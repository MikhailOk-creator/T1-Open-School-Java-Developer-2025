package ru.t1.okhapkin.taskmanager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.okhapkin.taskmanager.component.TaskProducer;
import ru.t1.okhapkin.taskmanager.dto.TaskRequestDTO;
import ru.t1.okhapkin.taskmanager.dto.TaskResponseDTO;
import ru.t1.okhapkin.taskmanager.entity.Task;
import ru.t1.okhapkin.taskmanager.entity.exception.TaskNotFoundException;
import ru.t1.okhapkin.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskProducer taskProducer;

    @Test
    @DisplayName("Checking the creation of a task")
    void createTask() {
        TaskRequestDTO taskDTO = new TaskRequestDTO("Test Task", "Test Description", 1L);
        when(taskRepository.save(any(Task.class))).thenReturn(new Task(UUID.randomUUID(), taskDTO.title(), taskDTO.description(), taskDTO.userID()));

        TaskResponseDTO createdTask = taskService.createTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals(taskDTO.title(), createdTask.title());
    }

    @Test
    @DisplayName("Checking a successful refund by id")
    void getTaskByIdSuccess() {
        Task task = new Task(UUID.randomUUID(), "Test Task", "Test Description", 1L);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        TaskResponseDTO foundTask = taskService.getTaskById(task.getId());

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.id());
    }

    @Test
    @DisplayName("Checking for an unsuccessful refund by id")
    void getTaskByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(randomId));
    }

    @Test
    @DisplayName("Checking for a successful update")
    void updateTaskSuccess() {
        Task task = new Task(UUID.randomUUID(), "Test Task", "Test Description", 1L);
        TaskRequestDTO taskDTO = new TaskRequestDTO("Test Task", "Updated Description", 1L);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponseDTO updatedTask = taskService.updateTask(task.getId(), taskDTO);

        assertNotNull(updatedTask);
        assertEquals(taskDTO.description(), updatedTask.description());
    }

    @Test
    @DisplayName("Checking for a failed update")
    void updateTaskNotFound() {
        UUID randomId = UUID.randomUUID();
        TaskRequestDTO taskDTO = new TaskRequestDTO("Test Task", "Updated Description", 1L);
        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(randomId, taskDTO));
    }

    @Test
    @DisplayName("Checking for successful deletion")
    void deleteTaskSuccess() {
        Task task = new Task(UUID.randomUUID(), "Test Task", "Test Description", 1L);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);
        taskService.deleteTask(task.getId());

        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Checking for failed deletion")
    void deleteTaskNotFound() {
        UUID randomId = UUID.randomUUID();
        when(taskRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(randomId));
    }

    @Test
    @DisplayName("Checking the tasks list output")
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task()));

        List<TaskResponseDTO> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
    }
}