package ru.t1.okhapkin.taskmanager.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.okhapkin.taskmanager.entity.Task;

@Component
public class TaskProducer {

    private final Logger log = LoggerFactory.getLogger(TaskProducer.class);

    @Value("${t1.kafka.topic.tasks_update}")
    private String TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TaskProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskUpdate(Task updatedTask) {
        try {
            String message = String.format("Updated task: ID - %s, title - %s, description - %s, user ID - %s",
                    updatedTask.getId(),
                    updatedTask.getTitle(),
                    updatedTask.getDescription(),
                    updatedTask.getUser());
            kafkaTemplate.send(TOPIC, message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
