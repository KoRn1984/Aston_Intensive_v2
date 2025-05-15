package ru.aston.matveenko_ym.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.matveenko_ym.dto.MessageDto;

@Slf4j
@Component
public class NotificationConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user-topic", groupId = "notification-group")
    public void listen(String message) {
        try {
            MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
            if ("CREATE".equalsIgnoreCase(messageDto.getOperation())) {
                emailService.sendEmail(messageDto.getEmail(),"Здравствуйте! Ваш аккаунт был успешно создан.");
            } else if ("DELETE".equalsIgnoreCase(messageDto.getOperation())) {
                emailService.sendEmail(messageDto.getEmail(),"Здравствуйте! Ваш аккаунт был удалён.");
            }
        } catch (Exception e) {
            log.error("Error sending email! {}", e.getMessage());
        }
    }
}