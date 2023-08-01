# Getting Started

### Reference Documentation
* Kafka launch : https://kafka.apache.org/quickstart
* SOAP Consumer : https://www.baeldung.com/spring-soap-web-service

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
