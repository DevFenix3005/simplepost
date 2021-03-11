package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.domain.entities.Greeting;
import com.rebirth.simplepost.services.GreetingService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final AtomicInteger counter = new AtomicInteger();
    private static final String TEMPLATE = "hello %s!";

    @Override
    public Greeting sayHello(String name) {
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }
}
