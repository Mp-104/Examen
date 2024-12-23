package com.example.examen.api;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
class RandomApi {

    private final org.springframework.web.reactive.function.client.WebClient webClient;

    public RandomApi(org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://uselessfacts.jsph.pl/api/v2/facts/")
                .build();
    }

    @GetMapping("/random")
    public String getRandomFact(Model model) {

        try {


            RandomFactModel randomFact = webClient.get()
                    .uri(uri -> uri
                            .path("random")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RuntimeException("Fel med clienten")))
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Fel med externa servern")))
                    .bodyToMono(RandomFactModel.class)
                    .block();

            model.addAttribute("fact", randomFact);

            return "fact";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

}
