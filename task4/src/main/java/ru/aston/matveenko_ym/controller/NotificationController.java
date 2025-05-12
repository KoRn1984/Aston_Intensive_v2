package ru.aston.matveenko_ym.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.matveenko_ym.service.EmailService;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        try {
            emailService.sendEmail(to, subject, text);
        } catch (Exception exception) {
            log.error("Error sending email: {}", exception.getMessage());
        }
        return "Email sent successfully!";
    }
}