package utc.englishlearning.Encybara.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import utc.englishlearning.Encybara.domain.Flashcard;
import utc.englishlearning.Encybara.domain.FlashcardGroup;
import utc.englishlearning.Encybara.domain.request.FlashcardGroupRequest;
import utc.englishlearning.Encybara.domain.response.FlashcardGroupResponse;
import utc.englishlearning.Encybara.service.FlashcardGroupService;
import utc.englishlearning.Encybara.domain.response.RestResponse;

@RestController
@RequestMapping("/api/v1/flashcard-groups")
public class FlashcardGroupController {

    @Autowired
    private FlashcardGroupService flashcardGroupService;

    @PostMapping
    public ResponseEntity<RestResponse<FlashcardGroupResponse>> createFlashcardGroup(
            @RequestBody FlashcardGroupRequest groupRequest) {
        FlashcardGroup newGroup = flashcardGroupService.createFlashcardGroup(groupRequest.getName(),
                groupRequest.getUserId());

        FlashcardGroupResponse responseDto = new FlashcardGroupResponse();
        responseDto.setId(newGroup.getId());
        responseDto.setName(newGroup.getName());
        responseDto.setUserId(newGroup.getUser().getId());

        RestResponse<FlashcardGroupResponse> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard group created successfully");
        response.setData(responseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<RestResponse<Void>> deleteFlashcardGroup(@PathVariable Long groupId) {
        flashcardGroupService.deleteFlashcardGroup(groupId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard group deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<RestResponse<Void>> updateFlashcardGroup(@PathVariable Long groupId,
            @RequestParam String newName) {
        flashcardGroupService.updateFlashcardGroup(groupId, newName);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard group updated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<RestResponse<Page<Flashcard>>> getFlashcardsInGroup(
            @PathVariable Long groupId,
            Pageable pageable,
            @RequestParam(required = false) String word,
            @RequestParam(required = false) Boolean learnedStatus,
            @RequestParam(required = false) String vietnameseMeaning) {
        Page<Flashcard> flashcards = flashcardGroupService.getFlashcardsInGroup(groupId, pageable, word, learnedStatus,
                vietnameseMeaning);
        RestResponse<Page<Flashcard>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcards retrieved successfully");
        response.setData(flashcards);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{flashcardId}/group/{groupId}")
    public ResponseEntity<RestResponse<Void>> addFlashcardToGroup(@PathVariable Long flashcardId,
            @PathVariable Long groupId) {
        flashcardGroupService.addFlashcardToGroup(flashcardId, groupId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard added to group successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{flashcardId}/group")
    public ResponseEntity<RestResponse<Void>> removeFlashcardFromGroup(@PathVariable Long flashcardId) {
        flashcardGroupService.removeFlashcardFromGroup(flashcardId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Flashcard removed from group successfully");
        return ResponseEntity.ok(response);
    }
}