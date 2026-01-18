package tony.redit_clone.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;

//import org.springframework.core.annotation.Order;

/**
 * Aspect that enforces rate limiting on methods annotated with
 * {@link RateLimited}.
 * Uses {@link Bucket} to manage token consumption.
 */
@Aspect
@Component
// @Order(100)
public class RateLimit {

    private final Bucket bucket;

    public RateLimit(Bucket publicKeyBucket) {
        this.bucket = publicKeyBucket;
    }

    /**
     * Intercepts method calls annotated with {@link RateLimited} to check for
     * available tokens.
     *
     * @param joinPoint the join point representing the intercepted method
     * @return the result of the method execution if a token is available
     * @throws Throwable if the target method throws an exception or if the rate
     *                   limit is exceeded
     */
    @Around("@annotation(tony.redit_clone.aspect.RateLimited)")
    public synchronized Object limit(ProceedingJoinPoint joinPoint) throws Throwable {
        String threadName = Thread.currentThread().getName();

        if (bucket.tryConsume(1)) {
            // Adding token count to log to verify it is decreasing
            System.out.println("[" + threadName + "] Token consumed. Remaining: " + bucket.getAvailableTokens());
            return joinPoint.proceed();
        }

        System.out.println("[" + threadName + "] Rate limit hit! Blocking this attempt.");
        throw new RuntimeException("Too many requests — rate limit exceeded");
    }
}