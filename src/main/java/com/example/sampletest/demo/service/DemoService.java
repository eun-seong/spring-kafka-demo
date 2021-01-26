package com.example.sampletest.demo.service;

import com.example.sampletest.demo.core.KafkaManager;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.SenderRecord;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;

@Service
public class DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);
    private static final String SERVICENAME = "demo";

    KafkaManager kafkaManager;
    Disposable disposable;

    public DemoService(KafkaManager kafkaManager) {
        this.kafkaManager = kafkaManager;
    }

    public Mono<String> start() {
        consume();
        produce();
        return Mono.just("START");
    }

    public Mono<String> stop() {
        dispose(disposable);

        return Mono.just("STOP");
    }

    protected void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /***** produce *****/
    protected void produce() {
        final Flux<SenderRecord<String, String, String>> records = generateSource()
                .doOnNext(comp -> logger.info("Create - name: {}\tmajor: {}",comp.getT1(), comp.getT2()))
                .map(Object::toString)
                .map(i -> SenderRecord.create(new ProducerRecord<>(SERVICENAME, i, i), i));

        kafkaManager.producer(records)
                .subscribe();
    }

    /***** consume *****/

    protected void consume() {
        disposable = kafkaManager.consumer(SERVICENAME)
                .subscribe();
    }


    /***** general *****/
    protected Flux<Tuple2<String, String>> generateSource(){
        return Flux.just(
                Tuples.of("eun", "computer"),
                Tuples.of("lobster", "statistics"),
                Tuples.of("zooho", "computer"),
                Tuples.of("hyeon", "mechanical"),
                Tuples.of("nayng", "electronic"))
                .delayElements(Duration.ofMillis(1000));
    }
}
