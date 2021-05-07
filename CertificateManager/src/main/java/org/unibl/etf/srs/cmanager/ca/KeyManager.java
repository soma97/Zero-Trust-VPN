package org.unibl.etf.srs.cmanager.ca;

import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMEncryptor;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;

public class KeyManager {

    private KeyManager() {}

    public static void writeKey(String fileName, String password, KeyPair keyPair) throws FileNotFoundException
    {
        PrintWriter printKey = new PrintWriter(Constants.CERTS_DIR + File.separator + fileName + ".key");
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(printKey))
        {
            PEMEncryptor encryptor = new JcePEMEncryptorBuilder(AlgorithmEnum.AES256CBC.toString()).build(password.toCharArray());
            JcaMiscPEMGenerator gen = new JcaMiscPEMGenerator(keyPair.getPrivate(), encryptor);
            pemWriter.writeObject(gen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey readKey(Provider provider, String fileName, String password) {
        try (PEMParser pemParser = new PEMParser(new FileReader(Constants.CERTS_DIR + File.separator + fileName + ".key")))
        {
            Object object = pemParser.readObject();
            PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(provider);
            KeyPair keyPair;
            if (object instanceof PEMEncryptedKeyPair) {
                keyPair = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
                return keyPair.getPrivate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
