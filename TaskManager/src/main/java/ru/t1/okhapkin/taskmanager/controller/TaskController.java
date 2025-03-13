package ru.t1.okhapkin.taskmanager.controller;

import org.springframework.web.bind.annotation.*;
import ru.t1.okhapkin.taskmanager.dto.TaskRequestDTO;
import ru.t1.okhapkin.taskmanager.dto.TaskResponseDTO;
import ru.t1.okhapkin.taskmanager.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody TaskRequestDTO task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable UUID id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable UUID id, @RequestBody TaskRequestDTO taskDetails) {
        return taskService.updateTask(id, taskDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    public List<TaskResponseDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

}
