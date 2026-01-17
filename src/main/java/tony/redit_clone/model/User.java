package tony.redit_clone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * This entity stores user account information including encrypted credentials
 * and account creation timestamp.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The encrypted account information (e.g., username or email).
     * Must be unique and non-null.
     */
    @Column(unique = true, nullable = false, length = 2000)
    private String encryptedAccount;

    /**
     * The encrypted password for the user account.
     * Non-null.
     */
    @Column(nullable = false, length = 2000)
    private String encryptedPassWord;

    /**
     * The timestamp when the user account was created.
     * Non-null.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Lifecycle method invoked before the entity is persisted.
     * Initializes the {@code createdAt} field to the current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
