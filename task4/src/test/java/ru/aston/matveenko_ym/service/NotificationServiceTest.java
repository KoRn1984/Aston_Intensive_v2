package ru.aston.matveenko_ym.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
        String message = "create:john.doe@example.com";
        notificationService.consume(message);

        verify(emailService, times(1))
                .sendEmail(eq("john.doe@example.com"), eq("Аккаунт создан"),
                        eq("Здравствуйте! Ваш аккаунт на сайте был успешно создан."));
    }

    @Test
    void testConsumeDeleteEvent() {
        String message = "delete:john.doe@example.com";
        notificationService.consume(message);

        verify(emailService, times(1))
                .sendEmail(eq("john.doe@example.com"), eq("Аккаунт удалён"),
                        eq("Здравствуйте! Ваш аккаунт был удалён."));
    }
}