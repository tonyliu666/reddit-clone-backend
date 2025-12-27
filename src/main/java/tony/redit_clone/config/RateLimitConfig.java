package tony.redit_clone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket publicKeyBucket() {
        // Defines a limit of 10 refills every 1 minute
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}