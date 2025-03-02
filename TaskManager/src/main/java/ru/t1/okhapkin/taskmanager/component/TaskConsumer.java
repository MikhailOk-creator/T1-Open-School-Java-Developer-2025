package ru.t1.okhapkin.taskmanager.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.okhapkin.taskmanager.service.NotificationService;

import java.util.List;

@Component
public class TaskConsumer {

    private final Logger log = LoggerFactory.getLogger(TaskConsumer.class);

    private final NotificationService notificationService;

    public TaskConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${t1.kafka.topic.tasks_update}",
            groupId = "${t1.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenTaskEvents(@Payload List<String> messages,
                                 Acknowledgment acknowledgment) {
        try {
            log.debug("Processing new messages containing task updates");
            notificationService.newMessages(messages);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }
    }

}
