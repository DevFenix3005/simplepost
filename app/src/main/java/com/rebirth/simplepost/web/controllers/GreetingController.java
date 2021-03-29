package com.rebirth.simplepost.web.controllers;

import com.rebirth.simplepost.services.dtos.Greeting;
import com.rebirth.simplepost.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(@Autowired GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Greeting> getGreeting(@PathVariable("name") String name) {
        Greeting greeting = this.greetingService.sayHello(name);
        return ResponseEntity.ok(greeting);
    }

}
