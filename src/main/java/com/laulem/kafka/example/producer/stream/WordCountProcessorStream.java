package com.laulem.kafka.example.producer.stream;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WordCountProcessorStream {
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Value("${config.kafka.topics1}")
    private String defaultTopic;
    @Value("${config.kafka.topics-stream-result}")
    private String streamResultTopic;

    @Autowired
    void buildPipeline(final StreamsBuilder streamsBuilder) {
        final KStream<String, String> messageStream = streamsBuilder.stream(defaultTopic, Consumed.with(STRING_SERDE, STRING_SERDE));
        final KTable<String, Long> wordCounts = messageStream.mapValues((ValueMapper<String, String>) String::toLowerCase).flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE)).count(Materialized.as("count"));

        wordCounts.toStream().mapValues(x -> {
            System.out.println("Word counts - v: " + x);
            return Long.toString(x);
        }).to(streamResultTopic);

        final KStream<String, String> messageStream2 = streamsBuilder.stream(defaultTopic, Consumed.with(STRING_SERDE, STRING_SERDE));

        final KStream<String, String> wordCounts2 = messageStream2.mapValues((ValueMapper<String, String>) String::toLowerCase);
        wordCounts2.foreach((k, v) -> System.out.println("Word counts 2 - k:" + k + " - v:" + v));
    }
}
