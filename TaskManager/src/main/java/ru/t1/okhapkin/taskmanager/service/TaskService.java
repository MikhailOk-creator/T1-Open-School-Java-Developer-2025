package ru.t1.okhapkin.taskmanager.service;

import org.springframework.stereotype.Service;
import ru.t1.okhapkin.taskmanager.aspect.annotaion.CustomTracking;
import ru.t1.okhapkin.taskmanager.component.TaskProducer;
import ru.t1.okhapkin.taskmanager.dto.TaskDTO;
import ru.t1.okhapkin.taskmanager.entity.Task;
import ru.t1.okhapkin.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProducer taskProducer;

    public TaskService(TaskRepository taskRepository, TaskProducer taskProducer) {
        this.taskRepository = taskRepository;
        this.taskProducer = taskProducer;
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
        return taskRepository.findById(idOfTask);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @CustomTracking
    public Task updateTask(UUID idOfOriginalTask, TaskDTO updatedTask) {
        Task task = taskRepository.findById(idOfOriginalTask).get();
        task.setTitle(updatedTask.title());
        task.setDescription(updatedTask.description());
        task.setUser(updatedTask.userID());
        Task updTask = taskRepository.save(task);
        taskProducer.sendTaskUpdate(task);
        return updTask;
    }

    public void deleteTask(UUID idOfTask) {
        taskRepository.deleteById(idOfTask);
    }
}
