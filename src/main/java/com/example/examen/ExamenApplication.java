package com.example.examen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ExamenApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamenApplication.class, args);
    }
}
