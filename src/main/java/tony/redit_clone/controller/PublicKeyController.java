package tony.redit_clone.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import tony.redit_clone.security.KeyProvider;

import java.security.PublicKey;
import java.util.Base64;

@RestController
public class PublicKeyController {

    private final KeyProvider keyProvider;

    public PublicKeyController(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    @GetMapping("/api/public-key")
    public String getPublicKey() {
        PublicKey publicKey = keyProvider.getPublicKey();
        String base64Key = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + base64Key + "\n-----END PUBLIC KEY-----";
    }
}
