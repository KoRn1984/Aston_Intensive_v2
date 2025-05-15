package ru.aston.matveenko_ym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.aston.matveenko_ym.dto.MessageDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.user-topic}")
    private String userTopic;

    public void sendMessage(MessageDto messageDto) {
        try {
            String message = objectMapper.writeValueAsString(messageDto);
            kafkaTemplate.send(userTopic, message);
        } catch (Exception e) {
            log.error("Error sending message to Kafka! {}", e.getMessage());
            throw new RuntimeException("Error sending message to Kafka!", e);
        }
    }
}