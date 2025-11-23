package tony.redit_clone.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tony.redit_clone.dto.UserRegistrationRequest;
import tony.redit_clone.model.User;
import tony.redit_clone.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // /api/v1/signup
    @PostMapping("/signup")
    public String registerUser(@RequestBody UserRegistrationRequest request) {

        // Check if user already exists
        if (userRepository.findByEncryptedAccount(request.getEncryptedAccount()).isPresent()) {
            return "User already exists";
        }

        // Create and save new user
        User user = new User();
        user.setEncryptedAccount(request.getEncryptedAccount());
        user.setEncryptedPass(request.getEncryptedPass());

        userRepository.save(user);

        return "User registered successfully";
    }
}
