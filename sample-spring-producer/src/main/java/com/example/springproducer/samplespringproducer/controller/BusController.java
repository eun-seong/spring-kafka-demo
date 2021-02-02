package com.example.springproducer.samplespringproducer.controller;

import com.example.springproducer.samplespringproducer.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("kafka")
public class BusController {
    private final ProducerService producerService;
    private AtomicBoolean running;

    public BusController(ProducerService producerService) {
        this.producerService = producerService;
        this.running = new AtomicBoolean(false);
    }

    @GetMapping("/producer/start")
    public String startProducing() {
        return running.compareAndSet(false, true)
                ? producerService.start()
                : "Already Running";
    }

    @GetMapping("/producer/stop")
    public String stopProducing() {
        return running.compareAndSet(true, false)
                ? producerService.stop()
                : "Not Running Now";
    }
}
