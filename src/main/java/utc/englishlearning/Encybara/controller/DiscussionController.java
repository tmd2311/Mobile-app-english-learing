package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.request.discussion.ReqCreateDiscussionDTO;
import utc.englishlearning.Encybara.domain.response.discussion.ResDiscussionDTO;
import utc.englishlearning.Encybara.service.DiscussionService;
import utc.englishlearning.Encybara.domain.response.RestResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discussions")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @PostMapping
    public ResponseEntity<RestResponse<ResDiscussionDTO>> createDiscussion(
            @RequestBody ReqCreateDiscussionDTO reqCreateDiscussionDTO) {
        ResDiscussionDTO discussionDTO = discussionService.createDiscussion(reqCreateDiscussionDTO);
        RestResponse<ResDiscussionDTO> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Discussion created successfully");
        response.setData(discussionDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<RestResponse<List<ResDiscussionDTO>>> getAllDiscussionsByLessonId(
            @PathVariable Long lessonId) {
        List<ResDiscussionDTO> discussions = discussionService.getAllDiscussionsByLessonId(lessonId);
        RestResponse<List<ResDiscussionDTO>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Discussions retrieved successfully");
        response.setData(discussions);
        return ResponseEntity.ok(response);
    }
}