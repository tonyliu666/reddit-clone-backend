package tony.redit_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tony.redit_clone.model.User;
import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 * Uses JPA (SQL) for persistent storage.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their encrypted account identifier.
     *
     * @param encryptedAccount the encrypted account string
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByEncryptedAccount(String encryptedAccount);
}
