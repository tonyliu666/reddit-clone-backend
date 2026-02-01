package tony.redit_clone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Example external service that can be injected into TaskExecutorService.
 * This demonstrates that logging works even when called from executor threads.
 */
@Service
public class ExampleTaskService {

    private static final Logger logger = LoggerFactory.getLogger(ExampleTaskService.class);

    /**
     * Example method that will be called from the executor thread.
     * Logging will work correctly here.
     */
    synchronized void performTask(String data) {
        logger.info("ExampleTaskService - Starting task: {}", data);

        // Simulate some work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Task interrupted", e);
        }

        // Simulate exceptions based on data
        if (data.contains("error")) {
            logger.error("ExampleTaskService - Error condition detected in: {}", data);
            throw new RuntimeException("Error in ExampleTaskService: " + data);
        }

        logger.info("ExampleTaskService - Task completed: {}", data);
    }
}
