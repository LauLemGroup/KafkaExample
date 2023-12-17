package com.laulem.kafka.example.producer.stream;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WordCountProcessorStream2 {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    final private KafkaStreamsConfiguration properties;

    public void buildPipeline() {
        /*
        // Topology
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> inputTopic = builder.stream("quickstart-events");
        KStream<String, String> processedStream = inputTopic.mapValues(value -> "Processed: " + value);

        processedStream.foreach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
        });

        KafkaStreams streams = new KafkaStreams(builder.build(), properties.asProperties());
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));*/
    }
}
