package tony.redit_clone.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tony.redit_clone.dto.UserRegistrationRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {
    // /api/v1/signup
    @PostMapping("/signup")
    public String registerUser(@RequestBody UserRegistrationRequest request) {
        // Registration logic here
        return request.getEncryptedAccount() + " " + request.getEncryptedPass();
    }
}
