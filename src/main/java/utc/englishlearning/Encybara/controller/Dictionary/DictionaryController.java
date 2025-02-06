package utc.englishlearning.Encybara.controller.Dictionary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import utc.englishlearning.Encybara.domain.response.dictionary.ResWord;
import utc.englishlearning.Encybara.domain.response.RestResponse;
import utc.englishlearning.Encybara.service.DictionaryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/{word}")
    public ResponseEntity<RestResponse<List<ResWord>>> getDefinition(@PathVariable String word) {
        Mono<List<ResWord>> definitionsMono = dictionaryService.getWordDefinition(word);

        // Chuyển đổi Mono<List<ResWord>> thành List<ResWord>
        List<ResWord> definitions = definitionsMono.block(); // Chặn để lấy giá trị

        RestResponse<List<ResWord>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Definitions retrieved successfully");
        response.setData(definitions);
        return ResponseEntity.ok(response);
    }
}
