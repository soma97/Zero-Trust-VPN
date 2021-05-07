package org.unibl.etf.srs.cmanager.ca;

import lombok.Getter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.security.*;
import java.security.cert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Component
public class CaManager {

    private String caFileName = "CA";
    private String caKeyFileName = "cakey";
    private String caPass = "passcakey";

    @Getter
    private X509Certificate caCertificate;
    @Getter
    private KeyPair caKeyPair;

    private X509CRL crl;

    @Getter
    private final Provider provider;

    public CaManager() {
        provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        createCaCertIfNotExists();
        createCRL();
    }

    private void createCRL() {
        try {
            // create cert and revoke it
            KeyPair keyPair = generateKeyPair(2048);
            X509Certificate certificate = createCertificate("cert-sample-crl", Constants.O, keyPair.getPublic(), null, false, 3);
            writeCertToFile(certificate, false);
            writeCRL(crl = loadCrlAndAddRevoked(certificate));
        } catch (IOException | CertificateException | CRLException | OperatorCreationException | NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
    }

    private void createCaCertIfNotExists() {
        File directory = new File(Constants.CERTS_DIR);
        if (!directory.exists()){
            directory.mkdir();
        }
        if (!Files.exists(new File(Constants.CERTS_DIR + File.separator + caFileName + ".cer").toPath(), LinkOption.NOFOLLOW_LINKS)) {
            try {
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance(AlgorithmEnum.RSA.toString());
                keyGen.initialize(3072);
                caKeyPair = keyGen.generateKeyPair();

                System.out.println("Creating CA certificate...");
                caCertificate = createCertificate("SRS-CM Root CA", Constants.O, caKeyPair.getPublic(), null, true, 5);
                writeCertToFile(caCertificate, true);
                KeyManager.writeKey(caKeyFileName, caPass, caKeyPair);
            }
            catch (IOException | NoSuchAlgorithmException | CertificateException | OperatorCreationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                caCertificate = readCertFromFile(caFileName);
                caKeyPair = new KeyPair(caCertificate.getPublicKey(), KeyManager.readKey(provider, caKeyFileName, caPass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public X509Certificate createCertificate(String cName, String organization, PublicKey publicKey, String domain, boolean isSelfSigned, int yearsValid) throws OperatorCreationException, CertIOException, CertificateException {
        String name = "C=BA" + ",O=" + organization + ",CN=" + cName;

        long now = System.currentTimeMillis();
        BigInteger certSerialNumber = new BigInteger(Long.toString(now));
        Date startDate = new Date(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, yearsValid);
        Date endDate = calendar.getTime();


        X500Name CAX500Name = isSelfSigned ? new X500Name(name) : X500Name.getInstance(caCertificate.getSubjectX500Principal().getEncoded());
        X500Name subjectX500Name = new X500Name(name);

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(CAX500Name,
                    certSerialNumber, startDate,
                    endDate, subjectX500Name, publicKey);

        certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(isSelfSigned));

        if (domain != null) {
            certBuilder.addExtension(Extension.subjectAlternativeName, false,
                    new GeneralNames(new GeneralName(GeneralName.dNSName, domain)));
        }

        ContentSigner contentSigner = new JcaContentSignerBuilder(AlgorithmEnum.SHA256WithRSA.toString())
                .build(caKeyPair.getPrivate());

        return new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));
    }

    public String checkInvalidOrRevokedStatus(X509Certificate certificate) {
        try {
            certificate.checkValidity();
            if(crl.isRevoked(certificate)) {
                X509CRLEntry revokedCert = crl.getRevokedCertificate(certificate.getSerialNumber());
                return "Certificate revoked: " + revokedCert.getRevocationDate() + " " + revokedCert.getRevocationReason().toString();
            }
        } catch (CertificateNotYetValidException | CertificateExpiredException e) {
            return (e instanceof CertificateNotYetValidException ? "Certificate not yet valid" : "Certificate expired");
        }
        return null;
    }

    private X509CRL loadCrlAndAddRevoked(X509Certificate... revoked) throws FileNotFoundException, CertificateException, CRLException, OperatorCreationException {
        X509v2CRLBuilder builder = new X509v2CRLBuilder(new X500Name(caCertificate.getSubjectDN().getName()), new Date());

        for (X509Certificate certificate : revoked) {
            builder.addCRLEntry(certificate.getSerialNumber(), new Date(), CRLReason.PRIVILEGE_WITHDRAWN.ordinal());
        }

        if (Files.exists(new File(Constants.CERTS_DIR + File.separatorChar + "CRL.crl").toPath(), LinkOption.NOFOLLOW_LINKS)) {
            FileInputStream in = new FileInputStream(Constants.CERTS_DIR + File.separatorChar + "CRL.crl");

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509CRL crl = (X509CRL) cf.generateCRL(in);
            Set<? extends X509CRLEntry> s = crl.getRevokedCertificates();

            if (s != null && !s.isEmpty()) {
                for (X509CRLEntry entry : s) {
                    builder.addCRLEntry(entry.getSerialNumber(), entry.getRevocationDate(), entry.getRevocationReason().ordinal());
                }
            }
        }

        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(AlgorithmEnum.SHA256WithRSA.toString());
        contentSignerBuilder.setProvider(provider);

        X509CRLHolder crlHolder = builder.build(contentSignerBuilder.build(caKeyPair.getPrivate()));
        JcaX509CRLConverter converter = new JcaX509CRLConverter();
        converter.setProvider(provider);

        return converter.getCRL(crlHolder);
    }

    public KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(AlgorithmEnum.RSA.toString());
        keyGen.initialize(keySize);
        return keyGen.generateKeyPair();
    }

    public void writeCRL(X509CRL crlList) throws IOException {
        for(X509CRLEntry entry : crlList.getRevokedCertificates()) {
            System.out.println(entry.getSerialNumber());
        }
        JcaPEMWriter pemWriter = new JcaPEMWriter(new PrintWriter(Constants.CERTS_DIR + File.separatorChar + "CRL.crl"));
        pemWriter.writeObject(crlList);
        pemWriter.close();
    }

    public X509Certificate readCertFromFile(String fileName) throws IOException, CertificateException {
        FileInputStream fileInput = new FileInputStream(Constants.CERTS_DIR + File.separator + fileName + ".cer");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(fileInput);
    }

    public void writeCertToFile(X509Certificate certificate, boolean isCA) throws IOException, CertificateEncodingException {
        JcaPEMWriter pemWriter = new JcaPEMWriter(new PrintWriter(Constants.CERTS_DIR + File.separator + (isCA ? caFileName : certificate.getSerialNumber().longValue()) + ".cer"));
        pemWriter.writeObject(certificate);
        pemWriter.close();
    }

}
