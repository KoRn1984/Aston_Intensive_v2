package ru.aston.matveenko_ym.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consume(String message) {
        String[] parts = message.split(":");
        String operation = parts[0];
        String email = parts[1];

        if ("create".equals(operation)) {
            emailService.sendEmail(email, "Аккаунт создан",
                    "Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
        } else if ("delete".equals(operation)) {
            emailService.sendEmail(email, "Аккаунт удалён",
                    "Здравствуйте! Ваш аккаунт был удалён.");
        }
    }
}