package ru.aston.matveenko_ym.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaProducerServiceTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    void testSendMessage() {
        kafkaProducerService.sendMessage("user-events", "create:test@example.com");
        // Убедитесь, что сообщение отправлено (например, через консоль Kafka)
    }
}