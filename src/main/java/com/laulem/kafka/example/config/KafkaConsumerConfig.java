package com.laulem.kafka.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${config.kafka.group.id}")
    private String groupId;
    @Value("${config.kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value(value = "${kafka.backoff.interval}")
    private Long interval;

    @Value(value = "${kafka.backoff.max_failure}")
    private Long maxAttempts;

    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff fixedBackOff = new ExponentialBackOff(interval, maxAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
            System.out.println("All retry executed - topic:" + consumerRecord.topic() + " - k:" + consumerRecord.key() + " - v:" + consumerRecord.value());
        }, fixedBackOff);

        errorHandler.addRetryableExceptions(NullPointerException.class);

        return errorHandler;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        final Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler()); // Blocking retry
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }
}