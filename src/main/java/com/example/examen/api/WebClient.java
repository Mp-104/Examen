package com.example.examen.api;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient.Builder;


// Todo probably should not be subclasses, this is just for testing purposes

@Component
public class WebClient {

    @Bean
    public org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder () {
        return org.springframework.web.reactive.function.client.WebClient.builder();
    }

}

class RandomFactModel {
    private String text;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

@Controller
class RandomApi {

    private final org.springframework.web.reactive.function.client.WebClient webClient;

    public RandomApi (org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://uselessfacts.jsph.pl/api/v2/facts/")
                .build();
    }

    @GetMapping("/random")
    public String getRandomFact (Model model) {

        RandomFactModel randomFact = webClient.get()
                .uri( uri -> uri
                        .path("random")
                        .build())
                .retrieve()
                .bodyToMono(RandomFactModel.class)
                .block();

        model.addAttribute("fact", randomFact);

        return "fact";
    }

}
