package com.example.springbatchstudy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringbatchstudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbatchstudyApplication.class, args);
    }

}
