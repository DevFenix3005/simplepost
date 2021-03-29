package com.rebirth.simplepost.services.impl;

import com.rebirth.simplepost.services.dtos.Greeting;
import com.rebirth.simplepost.services.GreetingService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class GreetingServiceImpl implements GreetingService {

    private final AtomicLong counter = new AtomicLong();
    private static final String TEMPLATE = "Hi %s!";

    @Override
    public Greeting sayHello(String name) {
        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }
}
