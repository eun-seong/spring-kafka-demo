package com.example.sampletest.demo.core;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.util.List;
import java.util.Map;

@Component
public class KafkaManager {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";   // kafka host
    private final Map<String, Object> consumerProps;                    // consumer settings
    private final Map<String, Object> producerProps;                    // producer settings

    public KafkaManager() {
        this.consumerProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS,
                ConsumerConfig.CLIENT_ID_CONFIG, "consumer",
                ConsumerConfig.GROUP_ID_CONFIG, "group",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.producerProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS,
                ProducerConfig.CLIENT_ID_CONFIG, "producer",
                ProducerConfig.ACKS_CONFIG, "all",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    }

    public Flux<SenderResult<String>> producer(final Publisher<? extends SenderRecord<String, String, String>> publisher) {
        final SenderOptions<String, String> options = SenderOptions.create(producerProps);

        return KafkaSender.create(options)
                .send(publisher);
    }

    public Flux<ReceiverRecord<String, String>> consumer(final String topic) {
        final ReceiverOptions<String, String> options = ReceiverOptions.<String, String>create(consumerProps)
                .subscription(List.of(topic));

        return KafkaReceiver.create(options)
                .receive();
    }
}
