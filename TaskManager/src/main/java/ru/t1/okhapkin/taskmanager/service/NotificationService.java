package ru.t1.okhapkin.taskmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void newMessages(List<String> messages) {
        messages.forEach(message -> log.info("New message: {}", message));
    }

}
