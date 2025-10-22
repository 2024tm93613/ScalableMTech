package com.payment.microservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from manually created microservice!";
    }

    @GetMapping("/test")
    public String getUsers() {
        return "Hello from manually created microserviceeeee!";
    }
}