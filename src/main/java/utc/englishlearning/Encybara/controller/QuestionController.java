package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.Question;
import utc.englishlearning.Encybara.service.QuestionService;
import utc.englishlearning.Encybara.domain.response.question.ResCreateQuestionDTO;
import utc.englishlearning.Encybara.domain.response.question.ResUpdateQuestionDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
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
        return ResponseEntity.ok(updatedQuestionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}