package com.example.examen.api;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;


// Todo probably should not be subclasses, this is just for testing purposes

@Component
public class WebClient {

    @Bean
    public org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder () {
        return org.springframework.web.reactive.function.client.WebClient.builder();
    }




}

