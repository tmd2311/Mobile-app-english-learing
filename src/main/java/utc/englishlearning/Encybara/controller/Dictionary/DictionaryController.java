package utc.englishlearning.Encybara.controller.Dictionary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import utc.englishlearning.Encybara.domain.response.dictionary.Phonetic;
import utc.englishlearning.Encybara.domain.response.dictionary.ResWord;
import utc.englishlearning.Encybara.service.DictionaryService;

import java.util.Dictionary;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/{word}")
    public Mono<List<ResWord>> getDefinition(@PathVariable String word) {
        return dictionaryService.getWordDefinition(word);
    }

}
