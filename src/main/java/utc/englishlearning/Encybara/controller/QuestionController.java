package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.Question;
import utc.englishlearning.Encybara.service.QuestionService;
import utc.englishlearning.Encybara.domain.response.question.ResCreateQuestionDTO;
import utc.englishlearning.Encybara.domain.response.question.ResUpdateQuestionDTO;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @RequestBody ResCreateQuestionDTO questionDTO) {
        Question createdQuestion = questionService.createQuestion(questionDTO);
        return ResponseEntity.ok(createdQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResUpdateQuestionDTO> updateQuestion(@PathVariable Long id,
            @RequestBody ResUpdateQuestionDTO questionDTO) {
        questionDTO.setId(id);
        ResUpdateQuestionDTO updatedQuestionDTO = questionService.updateQuestion(id, questionDTO);
        if (updatedQuestionDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedQuestionDTO);
    }

}