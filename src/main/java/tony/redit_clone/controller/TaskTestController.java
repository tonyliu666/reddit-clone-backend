package tony.redit_clone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tony.redit_clone.service.TaskExecutorService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for testing the TaskExecutorService.
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskTestController {

    @Autowired
    private TaskExecutorService taskExecutorService;

    /**
     * Submits a task for processing.
     * 
     * @param taskData The task data to process
     * @return Response indicating whether the task was submitted
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitTask(@RequestBody String taskData) {
        boolean submitted = taskExecutorService.submitTask(taskData);

        Map<String, Object> response = new HashMap<>();
        response.put("submitted", submitted);
        response.put("taskData", taskData);

        return ResponseEntity.ok(response);
    }
}
