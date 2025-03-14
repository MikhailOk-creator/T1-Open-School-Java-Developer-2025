package ru.t1.okhapkin.taskmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.okhapkin.taskmanager.PostgresContainer;
import ru.t1.okhapkin.taskmanager.component.TaskProducer;
import ru.t1.okhapkin.taskmanager.dto.TaskRequestDTO;
import ru.t1.okhapkin.taskmanager.entity.Task;
import ru.t1.okhapkin.taskmanager.repository.TaskRepository;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest extends PostgresContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskProducer taskProducer;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Checking the creation of a task")
    void testCreateTask() throws Exception {
        TaskRequestDTO taskDTO = new TaskRequestDTO("New Task", "New Description", 2L);
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDTO.title()));
    }

    @Test
    @DisplayName("Checking a successful refund by id")
    void testGetTaskById_Success() throws Exception {
        Task task = new Task(UUID.randomUUID(), "Test Task", "Test Description", 1L);
        taskRepository.save(task);
        mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(task.getTitle()));
    }

    @Test
    @DisplayName("Checking for an unsuccessful refund by id")
    void testGetTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/tasks/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Checking for a successful update")
    void testUpdateTask_Success() throws Exception {
        Task task = new Task(UUID.randomUUID(), "Test Task", "Test Description", 1L);
        taskRepository.save(task);
        TaskRequestDTO taskDTO = new TaskRequestDTO("Updated Task", "Updated Description", 1L);
        mockMvc.perform(put("/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDTO.title()));
    }

    @Test
    @DisplayName("Checking for a failed update")
    void testUpdateTask_NotFound() throws Exception {
        TaskRequestDTO taskDTO = new TaskRequestDTO("Updated Task", "Updated Description", 1L);
        mockMvc.perform(put("/tasks/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Checking for successful deletion")
    void testDeleteTask_Success() throws Exception {
        Task task = new Task(UUID.randomUUID(), "Test Task", "Test Description", 1L);
        taskRepository.save(task);
        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Checking for failed deletion")
    void testDeleteTask_NotFound() throws Exception {
        mockMvc.perform(delete("/tasks/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
