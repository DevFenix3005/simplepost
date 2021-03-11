package com.rebirth.simplepost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SimplepostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplepostApplication.class, args);
    }


}
