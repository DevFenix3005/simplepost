package com.rebirth.simplepost.configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.rebirth.simplepost.utils.Constants;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfiguration {


    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public ProblemModule problemModule() {
        return new ProblemModule();
    }

    @Bean
    public ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(
            JavaTimeModule javaTimeModule,
            ProblemModule problemModule,
            ConstraintViolationProblemModule constraintViolationProblemModule

    ) {
        return builder -> {
            builder.modules(javaTimeModule, problemModule, constraintViolationProblemModule);
            builder.simpleDateFormat(Constants.dateTimeFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(Constants.dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(Constants.dateTimeFormat)));
        };
    }


}
