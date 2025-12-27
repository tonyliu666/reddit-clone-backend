package tony.redit_clone.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tony.redit_clone.dto.UserRegistrationRequest;
import tony.redit_clone.model.User;
import tony.redit_clone.repository.UserRepository;
import java.util.Optional;

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
            throw new IllegalArgumentException("User already exists");
        }
        // Create and save new user
        User user = new User();
        user.setEncryptedAccount(request.getEncryptedAccount());
        user.setEncryptedPassWord(request.getEncryptedPassWord());

        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserRegistrationRequest request) {
        Optional<User> user = userRepository.findByEncryptedAccount(request.getEncryptedAccount());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (!user.get().getEncryptedPassWord().equals(request.getEncryptedPassWord())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return "User logged in successfully";

    }
}
