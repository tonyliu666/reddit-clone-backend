package tony.redit_clone.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class KeyProviderTests {

    @InjectMocks
    private KeyProvider keyProvider;

    @Test
    public void testReadPemFile() throws Exception {
        // Use reflection to call the private method readPemFile
        Method method = KeyProvider.class.getDeclaredMethod("readPemFile", String.class);
        method.setAccessible(true);
        byte[] result = (byte[]) method.invoke(keyProvider, "keys/public.pem");

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }
}
