# KafkaTest - LauLem.com

## Getting Started

### Reference Documentation
* Kafka launch : https://kafka.apache.org/quickstart

### Kafka launch
```
#  Download the latest Kafka release and extract it:
tar -xzf kafka_2.13-3.5.0.tgz
cd kafka_2.13-3.5.0

# Terminal 1 - Start the ZooKeeper service
bin/zookeeper-server-start.sh config/zookeeper.properties

# Terminal 2 - Start the Kafka broker service
bin/kafka-server-start.sh config/server.properties

# Terminal 3
# Create topic
bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
# Write some messages in the topic
bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092

# Terminal 4 - Read the messages from the topic
bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
```

### Tools
```
# Describe (stat) a topic
bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092

# Resize partition
./bin/kafka-topics.sh --alter --bootstrap-server localhost:9092 --topic quickstart-events --partitions 20

# Consumer with long as value and print key
bin/kafka-console-consumer.sh --topic output-topic3 --from-beginning --bootstrap-server localhost:9092 --value-deserializer=org.apache.kafka.common.serialization.LongDeserializer -property print.key=true
```

### Terminology
- A **topic** is a category of messages.
- A **partition** is a subdivision of a topic allowing for parallel processing.
- A **key** is a means of directing messages to specific partitions to ensure order or consistency for certain types of data.
- A **group ID** identifies a consumer group in Kafka. All consumers sharing the same Group ID are considered part of the same consumer group. Used for offset management and distributing messages among consumers. Kafka ensures that each message from a partition is consumed by only one consumer in the group.
- A **consumer ID** is a unique identifier for each consumer within a group. It is usually assigned automatically by Kafka and can be based on information such as the consumer's IP address and port. Used to individually identify consumers within a group, particularly useful during consumer rebalancing and for tracking performance or issues.

### Kafka tools
- **Apache Kafka Core**: This is the basic Kafka system, including the Kafka broker server, message producers and consumers, and topic managers.
- **Kafka Streams**: A data streaming framework for building real-time data processing applications. Kafka Streams enables the transformation, aggregation, and processing of data continuously directly from Kafka topics.
- **Kafka Connect** (not in this project): A tool for connecting Kafka with various data sources and destinations (such as databases, file systems, etc.). Kafka Connect is used for importing data into Kafka and exporting data from Kafka to other systems.

### URL
- Kafka: https://kafka.apache.org/
- Tutorials: https://www.baeldung.com/apache-kafka / https://codingharbour.com/apache-kafka/what-is-a-consumer-group-in-kafka/
- Cluster: https://www.confluent.io/fr-fr/blog/what-is-an-apache-kafka-cluster/ / https://kafka.apache.org/documentation/#basic_ops_cluster_expansion