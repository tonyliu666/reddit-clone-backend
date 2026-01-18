package tony.redit_clone.dto;

import lombok.Data;

/**
 * Data Transfer Object for user registration and login requests.
 * Carries encrypted account credentials received from the client.
 */
@Data
public class UserRegistrationRequest {
    private String encryptedAccount;
    private String encryptedPassWord;
}
