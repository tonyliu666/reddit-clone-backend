package tony.redit_clone.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tony.redit_clone.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUserWithLongEncryptedFields() {
        String longString = "a".repeat(1000);

        User user = new User();
        user.setEncryptedAccount(longString);
        user.setEncryptedPassWord(longString);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEncryptedAccount()).hasSize(1000);
        assertThat(savedUser.getEncryptedPassWord()).hasSize(1000);
    }
}
