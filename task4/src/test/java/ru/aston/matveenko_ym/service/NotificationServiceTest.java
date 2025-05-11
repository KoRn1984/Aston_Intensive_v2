package ru.aston.matveenko_ym.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @Test
    void testConsumeCreateEvent() {
        String message = "create:test@example.com";
        notificationService.consume(message);

        verify(emailService, times(1))
                .sendEmail(eq("test@example.com"), eq("Аккаунт создан"),
                        eq("Здравствуйте! Ваш аккаунт на сайте был успешно создан."));
    }

    @Test
    void testConsumeDeleteEvent() {
        String message = "delete:test@example.com";
        notificationService.consume(message);

        verify(emailService, times(1))
                .sendEmail(eq("test@example.com"), eq("Аккаунт удалён"),
                        eq("Здравствуйте! Ваш аккаунт был удалён."));
    }
}