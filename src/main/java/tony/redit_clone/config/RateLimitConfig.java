package tony.redit_clone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;

/**
 * Configuration class for Rate Limiting.
 * Defines the {@link Bucket} beans used for rate limiting strategies.
 */
@Configuration
public class RateLimitConfig {

    /**
     * Creates a {@link Bucket} for public key rate limiting.
     * Configured with a capacity of 10 tokens and a refill rate of 10 tokens per
     * minute.
     *
     * @return the configured {@link Bucket} instance
     */
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