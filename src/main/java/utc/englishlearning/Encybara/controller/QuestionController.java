package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.englishlearning.Encybara.domain.Question;
import utc.englishlearning.Encybara.service.QuestionService;
import utc.englishlearning.Encybara.domain.response.question.ResCreateQuestionDTO;
import utc.englishlearning.Encybara.domain.response.question.ResUpdateQuestionDTO;
import utc.englishlearning.Encybara.specification.QuestionSpecification;
import utc.englishlearning.Encybara.util.constant.QuestionTypeEnum;

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

    @GetMapping
    public ResponseEntity<Page<Question>> getAllQuestions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) QuestionTypeEnum type,
            @RequestParam(required = false) Integer point,
            Pageable pageable) {

        Specification<Question> spec = Specification.where(QuestionSpecification.hasKeyword(keyword))
                .and(QuestionSpecification.hasQuesContent(content))
                .and(QuestionSpecification.hasQuesType(type));

        if (point != null) {
            spec = spec.and(QuestionSpecification.hasPoint(point));
        }

        Page<Question> questions = questionService.getAllQuestions(spec, pageable);
        return ResponseEntity.ok(questions);
    }

}