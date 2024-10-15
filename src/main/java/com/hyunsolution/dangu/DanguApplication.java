package com.hyunsolution.dangu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DanguApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanguApplication.class, args);
    }
}
