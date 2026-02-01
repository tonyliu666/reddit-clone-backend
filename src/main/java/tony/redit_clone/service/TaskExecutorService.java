package tony.redit_clone.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * Simple background service demonstrating exception handling with Future.
 * Shows that logging works correctly even when using injected external
 * services.
 */
@Service
public class TaskExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutorService.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = false;

    @Autowired
    private ExampleTaskService exampleTaskService;

    @PostConstruct
    public void startWorking() {
        running = true;
        Thread workerThread = new Thread(this::processTaskQueue);
        workerThread.setName("TaskQueue-Worker");
        workerThread.start();
    }

    /**
     * Main loop that processes tasks from the queue.
     */
    private void processTaskQueue() {
        while (running) {
            try {
                String taskData = taskQueue.take(); // Blocking wait for task
                executeTask(taskData);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Executes a single task and handles any exceptions.
     */
    private void executeTask(String taskData) {
        Future<?> future = executor.submit(() -> processTask(taskData));

        try {
            // Block indefinitely until task completes (no timeout)
            future.get();
            logger.info("Task completed successfully: {}", taskData);

        } catch (ExecutionException e) {
            // Catch exceptions thrown by the task
            logger.error("Task failed: {}, Exception: {}",
                    taskData, e.getCause().getMessage(), e.getCause());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Task execution interrupted: {}", taskData);
        }
    }

    /**
     * Processes the actual task logic.
     * Delegates to injected ExampleTaskService - logging will work correctly!
     */
    private void processTask(String data) {
        logger.info("TaskExecutorService - Delegating to ExampleTaskService: {}", data);

        // Call the injected external service
        // All logging in ExampleTaskService will work correctly
        exampleTaskService.performTask(data);

        logger.info("TaskExecutorService - Task delegation completed: {}", data);
    }

    public boolean submitTask(String taskData) {
        return taskQueue.offer(taskData);
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        executor.shutdown();
    }
}
