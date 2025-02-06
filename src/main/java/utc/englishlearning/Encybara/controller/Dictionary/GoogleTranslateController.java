package utc.englishlearning.Encybara.controller.Dictionary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import utc.englishlearning.Encybara.service.GoogleTranslateService;

import java.util.Dictionary;

@RestController
@RequestMapping("/api/v1/dictionary")
public class GoogleTranslateController {
    private final GoogleTranslateService googleTranslateService;

    public GoogleTranslateController(GoogleTranslateService googleTranslateService) {
        this.googleTranslateService = googleTranslateService;
    }
    @GetMapping
    public Mono<String> translateText (
            @RequestParam String text,
            @RequestParam(defaultValue = "vi") String language
    ){
        return googleTranslateService.translate(text, language);
    }
}
