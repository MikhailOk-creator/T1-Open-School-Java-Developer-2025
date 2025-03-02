package ru.t1.okhapkin.taskmanager.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.okhapkin.taskmanager.dto.TaskKafkaDTO;
import ru.t1.okhapkin.taskmanager.entity.Task;

@Component
public class TaskProducer {

    private final Logger log = LoggerFactory.getLogger(TaskProducer.class);

    @Value("${t1.kafka.topic.tasks_update}")
    private String TOPIC;

    private final KafkaTemplate<String, TaskKafkaDTO> kafkaTemplate;

    public TaskProducer(KafkaTemplate<String, TaskKafkaDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskUpdate(Task updatedTask) {
        try {
            String message = String.format("Updated task: \n - title: %s; \n - description: %s; \n - user ID: %s",
                    updatedTask.getTitle(),
                    updatedTask.getDescription(),
                    updatedTask.getUser());

            kafkaTemplate.send(TOPIC, new TaskKafkaDTO(updatedTask.getId(), message));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
