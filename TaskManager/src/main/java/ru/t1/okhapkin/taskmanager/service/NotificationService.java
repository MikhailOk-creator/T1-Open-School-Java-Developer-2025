package ru.t1.okhapkin.taskmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.t1.okhapkin.taskmanager.dto.TaskKafkaDTO;

import java.util.List;

@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Value("${spring.mail.username}")
    private String emailForm;

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void newMessages(List<TaskKafkaDTO> messages) {
        messages.forEach(message ->
                sendEmail(emailForm, "The task with the ID " + message.id() + " has been updated", message.changes())
        );
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailForm);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        log.info("Notification send to mail: {}", to);
    }

}
