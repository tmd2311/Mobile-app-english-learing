package utc.englishlearning.Encybara.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GoogleTranslateService {
    private final WebClient webClient;
    private static final String API_KEY = "AIzaSyAWzERmv1eOTgeplOofls-qGgivOj8SEWM";

    public GoogleTranslateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://translation.googleapis.com/language/translate/v2").build();
    }
    public Mono<String> translate(String text, String language) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", API_KEY)
                        .build())
                .bodyValue(Map.of(
                        "q", List.of(text),
                        "target", language
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> translations = (List<Map<String, Object>>) ((Map<String, Object>) response.get("data")).get("translations") ;
                    return (String) translations.get(0).get("translatedText");
                });
    }
}
