package com.example.sampletest.demo.controller;

import com.example.sampletest.demo.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/")
public class DemoController {
    private DemoService service;        // 해당 서비스를 실행한다.
    private AtomicBoolean running;      // 서버 상태를 체크하고 변경하는 동기화 변수이다.

    public DemoController(DemoService service) {
        this.service = service;
        this.running = new AtomicBoolean(false);
    }

    @GetMapping("/start")
    public Mono<String> start() {
        // 서버가 실행 중이지 않을 경우 해당값을 true로 변경하고 서버를 실행한다.
        return running.compareAndSet(false, true)
                ? service.start()
                : Mono.just("Already Running");
    }

    @GetMapping("/stop")
    public Mono<String> stop() {
        // 서버가 실행 중 경우 해당값을 false로 변경하고 서버를 중지한다.
        return running.compareAndSet(true, false)
                ? service.stop()
                : Mono.just("Not Running Now");
    }
}
