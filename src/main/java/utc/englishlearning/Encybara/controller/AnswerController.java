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

import java.util.List;

@RestController
@RequestMapping("/api/v1/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<ResAnswerDTO> createAnswer(@RequestBody ReqCreateAnswerDTO reqCreateAnswerDTO) {
        ResAnswerDTO createdAnswer = answerService.createAnswer(reqCreateAnswerDTO);
        return ResponseEntity.ok(createdAnswer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResAnswerDTO> getAnswerById(@PathVariable Long id) {
        ResAnswerDTO answer = answerService.getAnswerById(id);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/question/{questionId}/user/{userId}")
    public ResponseEntity<Page<Answer>> getAllAnswersByQuestionIdAndUserId(@PathVariable Long questionId,
            @PathVariable Long userId, Pageable pageable) {
        Page<Answer> answers = answerService.getAllAnswersByQuestionIdAndUserId(questionId, userId, pageable);
        return ResponseEntity.ok(answers);
    }

    @PostMapping("/grade/{answerId}")
    public ResponseEntity<Void> gradeAnswer(@PathVariable Long answerId) {
        answerService.gradeAnswer(answerId);
        return ResponseEntity.noContent().build();
    }
}