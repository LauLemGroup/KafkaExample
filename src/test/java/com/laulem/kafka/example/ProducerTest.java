package com.laulem.kafka.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class ProducerTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${config.kafka.topics1}")
    private String defaultTopic;

    @Test
    void sendMessage() {
        kafkaTemplate.send(defaultTopic, "Hello");
    }

    @Test
    public void sendMessageAndGetOffset() {
        final String valueToSend = "The data";
        final String keyToSend = "The key";

        final CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(defaultTopic, keyToSend, valueToSend);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + valueToSend + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + keyToSend + "] due to : " + ex.getMessage());
            }
        });
    }
}
