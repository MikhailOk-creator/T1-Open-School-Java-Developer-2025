package ru.t1.okhapkin.taskmanager.service;

import org.springframework.stereotype.Service;
import ru.t1.okhapkin.taskmanager.aspect.annotaion.CustomTracking;
import ru.t1.okhapkin.taskmanager.dto.TaskDTO;
import ru.t1.okhapkin.taskmanager.entity.Task;
import ru.t1.okhapkin.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @CustomTracking
    public Task createTask(TaskDTO taskDTO) {
        return taskRepository.save(new Task(
                UUID.randomUUID(),
                taskDTO.title(),
                taskDTO.description(),
                taskDTO.userID()
        ));
    }

    @CustomTracking
    public Optional<Task> getTaskById(UUID idOfTask) {
        Optional<Task> originalTask = taskRepository.findById(idOfTask);
        if (originalTask.isPresent()) {
            return originalTask;
        } else {
            throw new RuntimeException("Task not found with id " + idOfTask);
        }
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @CustomTracking
    public Task updateTask(UUID idOfOriginalTask, TaskDTO updatedTask) {
        Optional<Task> originalTask = getTaskById(idOfOriginalTask);
        if (originalTask.isPresent()) {
            Task task = originalTask.get();
            task.setTitle(updatedTask.title());
            task.setDescription(updatedTask.description());
            task.setUser(updatedTask.userID());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found with id " + idOfOriginalTask);
        }
    }

    public void deleteTask(UUID idOfTask) {
        Optional<Task> originalTask = getTaskById(idOfTask);
        if (originalTask.isPresent()) {
            taskRepository.deleteById(idOfTask);
        } else {
            throw new RuntimeException("Task not found with id " + idOfTask);
        }
    }
}
