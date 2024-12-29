package com.example.examen.api;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class ApiController {

    private final WebClient webClient;

    public ApiController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://uselessfacts.jsph.pl/api/v2/facts/")
                .build();
    }

    @GetMapping("/random")
    public String getRandomFact (Model model) {


        try {


            RandomFact randomFact = webClient.get()
                    .uri(uri -> uri
                            .path("random")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RuntimeException("Fel med clienten")))
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Fel med externa servern")))
                    .bodyToMono(RandomFact.class)
                    .block();

            model.addAttribute("fact", randomFact);

            return "fact";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }


}
