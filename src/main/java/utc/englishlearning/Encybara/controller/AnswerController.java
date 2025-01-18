package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.Answer;
import utc.englishlearning.Encybara.domain.response.answer.ResAnswerDTO;
import utc.englishlearning.Encybara.domain.request.answer.ReqCreateAnswerDTO;
import utc.englishlearning.Encybara.service.AnswerService;
import utc.englishlearning.Encybara.domain.response.RestResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<RestResponse<ResAnswerDTO>> createAnswer(@RequestBody ReqCreateAnswerDTO reqCreateAnswerDTO) {
        ResAnswerDTO createdAnswer = answerService.createAnswer(reqCreateAnswerDTO);
        RestResponse<ResAnswerDTO> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Answer created successfully");
        response.setData(createdAnswer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ResAnswerDTO>> getAnswerById(@PathVariable Long id) {
        ResAnswerDTO answer = answerService.getAnswerById(id);
        RestResponse<ResAnswerDTO> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Answer retrieved successfully");
        response.setData(answer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<RestResponse<List<Answer>>> getAnswersByQuestionId(@PathVariable Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        RestResponse<List<Answer>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Answers retrieved successfully");
        response.setData(answers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/question/{questionId}/user/{userId}")
    public ResponseEntity<RestResponse<Page<Answer>>> getAllAnswersByQuestionIdAndUserId(@PathVariable Long questionId,
            @PathVariable Long userId, Pageable pageable) {
        Page<Answer> answers = answerService.getAllAnswersByQuestionIdAndUserId(questionId, userId, pageable);
        RestResponse<Page<Answer>> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Answers retrieved successfully");
        response.setData(answers);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/grade/{answerId}")
    public ResponseEntity<RestResponse<Void>> gradeAnswer(@PathVariable Long answerId) {
        answerService.gradeAnswer(answerId);
        RestResponse<Void> response = new RestResponse<>();
        response.setStatusCode(204);
        response.setMessage("Answer graded successfully");
        return ResponseEntity.noContent().build();
    }
}