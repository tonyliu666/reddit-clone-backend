package tony.redit_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tony.redit_clone.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEncryptedAccount(String encryptedAccount);
}
