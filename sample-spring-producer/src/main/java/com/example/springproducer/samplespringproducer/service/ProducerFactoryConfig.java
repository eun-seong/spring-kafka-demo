package com.example.springproducer.samplespringproducer.service;

import com.example.springproducer.samplespringproducer.model.BusData;
import com.example.springproducer.samplespringproducer.repository.BusRepository;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ProducerFactoryConfig {
    private DataSource dataSource;

    public ProducerFactoryConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ProducerService producerService() {
        return new ProducerService(kafkaTemplate(), busRepository(), taskScheduler());
    }

    @Bean
    public ProducerFactory<String, List<BusData>> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 3072000);
        return props;
    }

    @Bean
    public KafkaTemplate<String, List<BusData>> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public BusRepository busRepository() {
        return new BusRepository(dataSource);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
