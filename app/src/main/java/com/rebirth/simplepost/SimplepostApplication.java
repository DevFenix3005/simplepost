package com.rebirth.simplepost;

import com.rebirth.simplepost.configuration.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties({AppConfiguration.class})
public class SimplepostApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplepostApplication.class, args);
    }


}
