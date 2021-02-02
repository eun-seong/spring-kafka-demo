package com.example.springproducer.samplespringproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SampleSpringProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleSpringProducerApplication.class, args);
	}

}
