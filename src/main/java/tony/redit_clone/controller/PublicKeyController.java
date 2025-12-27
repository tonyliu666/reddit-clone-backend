package tony.redit_clone.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tony.redit_clone.security.KeyProvider;
import tony.redit_clone.service.CommunityService;

import java.security.PublicKey;
import java.util.Base64;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class PublicKeyController {

    private final KeyProvider keyProvider;
    private final CommunityService communityService;

    public PublicKeyController(KeyProvider keyProvider, CommunityService communityService) {
        this.keyProvider = keyProvider;
        this.communityService = communityService;
    }

    @GetMapping("/public-key")
    public String getPublicKey() {
        PublicKey publicKey = keyProvider.getPublicKey();
        String base64Key = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + base64Key + "\n-----END PUBLIC KEY-----";
    }
    // call community service test retry endpoint
    @GetMapping("/test-retry")
    public String testRetry() {
        // use current time stamp
        //long timestamp = System.currentTimeMillis();
        try {
            communityService.testRetry();
            // count time difference
            // long duration = System.currentTimeMillis() - timestamp;
            // System.out.println("Retry test completed in " + duration + " ms");
            // return "Retry test completed in " + duration + " ms";
            return "hello";
        } catch (Exception e) {
            // count time difference even on error
            // long duration = System.currentTimeMillis() - timestamp;
            // System.out.println(duration);
            return "hello";
        }
    }
}
