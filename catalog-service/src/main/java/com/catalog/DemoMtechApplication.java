package com.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.catalog"})
public class DemoMtechApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoMtechApplication.class, args);
    }
}