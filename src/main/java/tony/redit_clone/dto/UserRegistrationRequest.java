package tony.redit_clone.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String encryptedAccount;
    private String encryptedPassWord;
}
