package com.laulem.kafka.example.controller;

import com.laulem.kafka.example.producer.stream.WordCountProcessorStream2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class kafkaController {
    final private WordCountProcessorStream2 wordCountProcessor2;
    final private String defaultTopic;
    final private KafkaTemplate<String, String> kafkaTemplate;
    final private StreamsBuilderFactoryBean factoryBean;

    public kafkaController(final WordCountProcessorStream2 wordCountProcessor2, @Value("${config.kafka.topics1}") final String defaultTopic, final KafkaTemplate<String, String> kafkaTemplate,
                           final StreamsBuilderFactoryBean factoryBean) {
        this.wordCountProcessor2 = wordCountProcessor2;
        this.defaultTopic = defaultTopic;
        this.kafkaTemplate = kafkaTemplate;
        this.factoryBean = factoryBean;
    }

    @GetMapping("/count/{word}")
    public Long getWordCount(@PathVariable final String word) {
        return (Long) Optional.ofNullable(factoryBean.getKafkaStreams())
                .map(stream -> stream.store(StoreQueryParameters.fromNameAndType("count", QueryableStoreTypes.keyValueStore())))
                .map(counts -> counts.get(word))
                .orElse(null);
    }

    @GetMapping("/log")
    public void getWordCount() {
        wordCountProcessor2.buildPipeline();
    }

    @PostMapping(value = "/message")
    public void addMessage(@RequestBody final String message) {
        List<Header> headers = new ArrayList<>();
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(0);
        headers.add(new RecordHeader("retry-attempts", bb.array()));
        ProducerRecord <String, String> record = new ProducerRecord <>(defaultTopic, null, "", message, headers);
        kafkaTemplate.send(record);
    }
}
