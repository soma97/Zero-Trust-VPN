package org.unibl.etf.srs.cmanager.controller;

import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.srs.cmanager.ca.CaManager;
import org.unibl.etf.srs.cmanager.ca.Constants;
import org.unibl.etf.srs.cmanager.ca.KeyManager;
import org.unibl.etf.srs.cmanager.model.CertStatus;
import org.unibl.etf.srs.cmanager.model.CertificateRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/certificate")
public class ManagerController {

    private final CaManager caManager;

    @Autowired
    public ManagerController(CaManager caManager) {
        this.caManager = caManager;
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getCertificate(@RequestParam Long serial) throws FileNotFoundException {
        return createResource(serial + ".cer");
    }

    @GetMapping("/status")
    public ResponseEntity<CertStatus> getCertificateStatus(@RequestParam Long serial) throws CertificateException, IOException {
        X509Certificate certificate = caManager.readCertFromFile(serial.toString());
        String reason = caManager.checkInvalidOrRevokedStatus(certificate);

        return ResponseEntity.ok(new CertStatus(certificate.getVersion(), certificate.getSigAlgName(), certificate.getSerialNumber().longValue(),
                certificate.getIssuerDN().getName(), certificate.getSubjectDN().getName(), certificate.getNotBefore(), certificate.getNotAfter(),
                certificate.getPublicKey().toString(), reason == null, reason));
    }

    @GetMapping("/revoked")
    public ResponseEntity<Resource> getRevokedCertificates() throws FileNotFoundException {
        return createResource("CRL.crl");
    }

    @PostMapping(produces="application/zip")
    public void createCertificate(@RequestBody @Valid CertificateRequest certRequest, HttpServletResponse response) throws IOException, CertificateException, OperatorCreationException, NoSuchAlgorithmException {
        // create certificate and key pair
        KeyPair keyPair = caManager.generateKeyPair(2048);
        X509Certificate certificate = caManager.createCertificate(certRequest.getName(), Constants.O, keyPair.getPublic(), certRequest.getDomain(), false, certRequest.getYearsValid());
        caManager.writeCertToFile(certificate, false);
        KeyManager.writeKey(certificate.getSerialNumber().toString(), certificate.getSerialNumber().toString().substring(0,5), keyPair);

        // read certificate and its private key
        List<File> files = Arrays.asList(new File(Constants.CERTS_DIR + File.separator + certificate.getSerialNumber().longValue() + ".cer"),
        new File(Constants.CERTS_DIR + File.separator + certificate.getSerialNumber() + ".key"));

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + certificate.getSerialNumber().longValue() + ", key_pass=" + certificate.getSerialNumber().toString().substring(0,5) + ".zip\"");

        // create zip file
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (File file : files) {
            FileSystemResource resource = new FileSystemResource(file);
            ZipEntry zipEntry = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
    }

    private ResponseEntity<Resource> createResource(String fileName) throws FileNotFoundException {
        File file = new File(Constants.CERTS_DIR + File.separator + fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName().replace(",", ""));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
