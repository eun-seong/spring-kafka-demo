package com.example.springproducer.samplespringproducer.service;

import com.example.springproducer.samplespringproducer.model.BusData;
import com.example.springproducer.samplespringproducer.repository.BusRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class ProducerService {
    private static final String TOPIC="BusData";
    private static final String LOAD_BUS_DATA="LOAD_BUS_DATA";
    private final KafkaTemplate<String, List<BusData>> kafkaTemplate;
    private final BusRepository busRepository;
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap;

    public ProducerService(KafkaTemplate<String,List<BusData>> kafkaTemplate,
                           BusRepository busRepository,
                           TaskScheduler taskScheduler) {
        this.kafkaTemplate = kafkaTemplate;
        this.busRepository = busRepository;
        this.taskScheduler = taskScheduler;
        this.scheduledFutureMap = new ConcurrentHashMap<>();
    }

    public void sendBustDataObjectListMessage() {
        List<BusData> busData = busRepository.selectAll();
        kafkaTemplate.send(TOPIC, busData);
    }

    public String start() {
        ScheduledFuture<?> task = taskScheduler.scheduleAtFixedRate(this::sendBustDataObjectListMessage, 10000L);
        scheduledFutureMap.put(LOAD_BUS_DATA, task);

        return "START";
    }

    public String stop() {
        scheduledFutureMap.get(LOAD_BUS_DATA).cancel(true);
        return "STOP";
    }
}
