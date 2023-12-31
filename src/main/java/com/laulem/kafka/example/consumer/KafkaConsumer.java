package com.laulem.kafka.example.consumer;

import org.springframework.kafka.annotation.*;
import org.springframework.kafka.retrytopic.RetryTopicHeaders;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
public class KafkaConsumer {
    @KafkaListener(topics = "#{'${config.kafka.topics1}'.split(',')}", groupId = "${config.kafka.group.id}")
    public void listenDefaultTopic(@Header(value = KafkaHeaders.RECEIVED_KEY, required = false) final String key, @Payload final String message) throws InterruptedException {
        System.out.println("Default topic - message - key :" + key + " - received: " + message);
    }

    @KafkaListener(topics = "#{'${config.kafka.topics1}'.split(',')}", groupId = "Y")
    @KafkaHandler // non-blocking retry
    @RetryableTopic(backoff = @Backoff(value = 300L), attempts = "3", autoCreateTopics = "false", include = {NullPointerException.class})
    public void listenRetryNonBlockingDefaultTopic(@Header(value = KafkaHeaders.RECEIVED_KEY, required = false) final String key, @Payload final String message, @Header(name = RetryTopicHeaders.DEFAULT_HEADER_ATTEMPTS, required = false) Integer attempt) throws IllegalAccessException {
        if (attempt == null){
            throw new NullPointerException("listenRetryNonBlockingDefaultTopic 1 ");
        }
        throw new NullPointerException("listenRetryNonBlockingDefaultTopic " + attempt);
    }

    @DltHandler
    public void dlt(@Header(value = KafkaHeaders.RECEIVED_KEY, required = false) final String key,
                    @Payload final String message,
                    @Header(KafkaHeaders.ORIGINAL_OFFSET) byte[] offset,
                    @Header(KafkaHeaders.EXCEPTION_FQCN) String descException,
                    @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stacktrace,
                    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage) throws IllegalAccessException {
        System.out.println("General DLT - message - key :" + key + " - received: " + message);
    }

    @KafkaListener(topics = "#{'${config.kafka.topics-stream-result}'.split(',')}", groupId = "X")
    public void listenStreamResult(@Header(value = KafkaHeaders.RECEIVED_KEY, required = false) final String key, @Payload final String value) {
        System.out.println("Stream result message - key :" + key + " - received: " + value);
    }
}