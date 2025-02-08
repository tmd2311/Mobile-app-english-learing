package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.Flashcard;
import utc.englishlearning.Encybara.domain.response.flashcard.ResFlashcardDTO;
import utc.englishlearning.Encybara.service.FlashcardService;
import utc.englishlearning.Encybara.domain.response.RestResponse;
import utc.englishlearning.Encybara.domain.request.flashcard.ReqFlashcardDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @PostMapping
    public ResponseEntity<RestResponse<ResFlashcardDTO>> createFlashcard(
            @RequestBody ReqFlashcardDTO reqFlashcardDTO) {
        ResFlashcardDTO flashcard = flashcardService.createFlashcard(
                reqFlashcardDTO.getWord(),
                reqFlashcardDTO.getDefinitionIndices(),
                reqFlashcardDTO.getMeaningIndices(),
                reqFlashcardDTO.getPhoneticIndices(),
                reqFlashcardDTO.getUserId());
        RestResponse<ResFlashcardDTO> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard created successfully");
        response.setData(flashcard);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{flashcardId}/group/{groupId}")
    public ResponseEntity<RestResponse<Void>> addFlashcardToGroup(@PathVariable Long flashcardId,
            @PathVariable Long groupId) {
        flashcardService.addFlashcardToGroup(flashcardId, groupId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard added to group successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{flashcardId}/group")
    public ResponseEntity<RestResponse<Void>> removeFlashcardFromGroup(@PathVariable Long flashcardId) {
        flashcardService.removeFlashcardFromGroup(flashcardId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard removed from group successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<RestResponse<Void>> deleteFlashcard(@PathVariable Long flashcardId) {
        flashcardService.deleteFlashcard(flashcardId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{flashcardId}/learned")
    public ResponseEntity<RestResponse<Void>> markFlashcardAsLearned(@PathVariable Long flashcardId) {
        flashcardService.markFlashcardAsLearned(flashcardId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard marked as learned successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/group/{groupId}")
    public ResponseEntity<RestResponse<Void>> deleteFlashcardGroup(@PathVariable Long groupId) {
        flashcardService.deleteFlashcardGroup(groupId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard group deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/group/{groupId}")
    public ResponseEntity<RestResponse<Void>> updateFlashcardGroup(@PathVariable Long groupId,
            @RequestParam String newName) {
        flashcardService.updateFlashcardGroup(groupId, newName);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard group updated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{flashcardId}")
    public ResponseEntity<RestResponse<ResFlashcardDTO>> getFlashcard(@PathVariable Long flashcardId) {
        ResFlashcardDTO flashcard = flashcardService.getFlashcard(flashcardId);
        RestResponse<ResFlashcardDTO> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard retrieved successfully");
        response.setData(flashcard);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<RestResponse<Page<Flashcard>>> getFlashcardsInGroup(
            @PathVariable Long groupId,
            Pageable pageable,
            @RequestParam(required = false) String word,
            @RequestParam(required = false) Boolean learnedStatus,
            @RequestParam(required = false) String vietnameseMeaning) {
        Page<Flashcard> flashcards = flashcardService.getFlashcardsInGroup(groupId, pageable, word, learnedStatus,
                vietnameseMeaning);
        RestResponse<Page<Flashcard>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcards retrieved successfully");
        response.setData(flashcards);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sorted/latest")
    public ResponseEntity<RestResponse<List<Flashcard>>> getAllFlashcardsSortedByLatest() {
        List<Flashcard> flashcards = flashcardService.getAllFlashcardsSortedByLatest();
        RestResponse<List<Flashcard>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcards sorted by latest retrieved successfully");
        response.setData(flashcards);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sorted/oldest")
    public ResponseEntity<RestResponse<List<Flashcard>>> getAllFlashcardsSortedByOldest() {
        List<Flashcard> flashcards = flashcardService.getAllFlashcardsSortedByOldest();
        RestResponse<List<Flashcard>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcards sorted by oldest retrieved successfully");
        response.setData(flashcards);
        return ResponseEntity.ok(response);
    }
}