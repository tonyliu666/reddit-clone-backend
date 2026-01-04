package tony.redit_clone.security;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Component
public class KeyProvider {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public KeyProvider() throws Exception {
        // Load the keys from PEM files
        byte[] publicBytes = readPemFile("./keys/public.pem");
        byte[] privateBytes = readPemFile("./keys/private.pem");

        // Parse public key
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        this.publicKey = factory.generatePublic(pubSpec);

        // Parse private key
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privateBytes);
        this.privateKey = factory.generatePrivate(privSpec);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private byte[] readPemFile(String path) throws Exception {
        String key = Files.readString(new ClassPathResource(path).getFile().toPath());
        key = key
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(key);
    }

    public String decrypt(String encryptedData) throws Exception {
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}
