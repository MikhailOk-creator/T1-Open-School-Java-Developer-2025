package ru.t1.okhapkin.taskmanager.service;

import org.springframework.stereotype.Service;
import ru.t1.okhapkin.mystarterlogs.aspect.annotaion.CustomTimeTracking;
import ru.t1.okhapkin.taskmanager.dto.TaskRequestDTO;
import ru.t1.okhapkin.taskmanager.dto.TaskResponseDTO;
import ru.t1.okhapkin.taskmanager.entity.exception.TaskNotFoundException;
import ru.t1.okhapkin.taskmanager.component.TaskProducer;
import ru.t1.okhapkin.taskmanager.entity.Task;
import ru.t1.okhapkin.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskProducer taskProducer;

    public TaskService(TaskRepository taskRepository, TaskProducer taskProducer) {
        this.taskRepository = taskRepository;
        this.taskProducer = taskProducer;
    }

    public final Function<Task, TaskResponseDTO> toTaskResponseDTO = (Task task) ->
            new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.getUser());

    @CustomTimeTracking
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO) {
        return toTaskResponseDTO.apply(
                taskRepository.save(
                        new Task(
                            taskDTO.title(),
                            taskDTO.description(),
                            taskDTO.userID()
                        )
                )
        );
    }

    @CustomTimeTracking
    public TaskResponseDTO getTaskById(UUID idOfTask) {
        return toTaskResponseDTO.apply(
                taskRepository.findById(idOfTask)
                        .orElseThrow(() -> new TaskNotFoundException(idOfTask))
        );
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream().map(toTaskResponseDTO).toList();
    }

    @CustomTimeTracking
    public TaskResponseDTO updateTask(UUID idOfOriginalTask, TaskRequestDTO updatedTask) {
        Task task = taskRepository.findById(idOfOriginalTask)
                .orElseThrow(() -> new TaskNotFoundException(idOfOriginalTask));
        task.setTitle(updatedTask.title());
        task.setDescription(updatedTask.description());
        task.setUser(updatedTask.userID());
        Task updTask = taskRepository.save(task);
        taskProducer.sendTaskUpdate(task);
        return toTaskResponseDTO.apply(updTask);
    }

    public void deleteTask(UUID idOfTask) {
        Task task = taskRepository.findById(idOfTask)
                .orElseThrow(() -> new TaskNotFoundException(idOfTask));
        taskRepository.delete(task);
    }
}
