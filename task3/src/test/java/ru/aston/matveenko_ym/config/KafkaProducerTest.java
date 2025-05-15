package ru.aston.matveenko_ym.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aston.matveenko_ym.dto.MessageDto;

@SpringBootTest
public class KafkaProducerTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    public void testSendKafkaMessage() {
        MessageDto messageDto = new MessageDto("test@example.com", "CREATE");
        kafkaProducer.sendMessage(messageDto);
    }
}