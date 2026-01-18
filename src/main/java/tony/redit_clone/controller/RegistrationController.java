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
import tony.redit_clone.security.KeyProvider;

/**
 * Controller for handling user registration and authentication.
 * Manages user sign-up and login workflows with credential
 * encryption/decryption.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    private final UserRepository userRepository;
    private final KeyProvider keyProvider;

    public RegistrationController(UserRepository userRepository, KeyProvider keyProvider) {
        this.userRepository = userRepository;
        this.keyProvider = keyProvider;
    }

    // /api/v1/signup
    /**
     * Registers a new user.
     * Validates if the user already exists and saves the encrypted credentials.
     *
     * @param request the registration request containing encrypted account and
     *                password
     * @return a success message
     * @throws IllegalArgumentException if the user already exists
     */
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

    /**
     * Authenticates a user.
     * Decrypts the stored and provided passwords to perform a comparison.
     *
     * @param request the login request containing encrypted account and password
     * @return a success message
     * @throws IllegalArgumentException if the user is not found or the password is
     *                                  invalid
     * @throws RuntimeException         if decryption fails
     */
    @PostMapping("/login")
    public String loginUser(@RequestBody UserRegistrationRequest request) {
        Optional<User> user = userRepository.findByEncryptedAccount(request.getEncryptedAccount());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        try {
            String decryptedStoredPassword = keyProvider.decrypt(user.get().getEncryptedPassWord());
            String decryptedRequestPassword = keyProvider.decrypt(request.getEncryptedPassWord());
            if (!decryptedStoredPassword.equals(decryptedRequestPassword)) {
                throw new IllegalArgumentException("Invalid password");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting password", e);
        }
        return "User logged in successfully";

    }
}
