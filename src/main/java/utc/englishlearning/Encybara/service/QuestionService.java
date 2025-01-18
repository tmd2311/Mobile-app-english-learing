package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utc.englishlearning.Encybara.domain.Question;
import utc.englishlearning.Encybara.repository.QuestionRepository;
import utc.englishlearning.Encybara.domain.Question_Choice;
import utc.englishlearning.Encybara.repository.QuestionChoiceRepository;
import utc.englishlearning.Encybara.domain.response.question.ResCreateQuestionDTO;
import utc.englishlearning.Encybara.domain.response.question.ResUpdateQuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public Question createQuestion(ResCreateQuestionDTO questionDTO) {
        Question question = new Question();
        // Thiết lập các thuộc tính từ DTO
        question.setQuesContent(questionDTO.getQuesContent());
        question.setKeyword(questionDTO.getKeyword());
        question.setQuesType(questionDTO.getQuesType());
        question.setPoint(questionDTO.getPoint());

        // Lưu câu hỏi vào bảng Question
        Question savedQuestion = questionRepository.save(question);
        List<Question_Choice> choices = questionDTO.getQuestionChoices();

        for (Question_Choice choice : choices) {
            choice.setQuestion(savedQuestion);
            // Lưu lựa chọn vào bảng Question_Choice
            questionChoiceRepository.save(choice);
        }
        return savedQuestion;
    }

    public ResUpdateQuestionDTO updateQuestion(ResUpdateQuestionDTO questionDTO) {
        Long id = questionDTO.getId();
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return null;
        }
        question.setQuesContent(questionDTO.getQuesContent());
        question.setKeyword(questionDTO.getKeyword());
        question.setQuesType(questionDTO.getQuesType());
        question.setPoint(questionDTO.getPoint());

        // Cập nhật câu hỏi
        Question updatedQuestion = questionRepository.save(question);

        // Cập nhật các lựa chọn câu hỏi
        List<Question_Choice> existingChoices = questionChoiceRepository.findByQuestionId(id);
        List<Question_Choice> choices = questionDTO.getQuestionChoices();

        for (Question_Choice existingChoice : existingChoices) {
            boolean exists = choices.stream()
                    .anyMatch(c -> c.getChoiceContent().equals(existingChoice.getChoiceContent()));
            if (!exists) {
                questionChoiceRepository.delete(existingChoice);
            }
        }

        for (Question_Choice choice : choices) {
            Question_Choice existingChoice = existingChoices.stream()
                    .filter(c -> c.getChoiceContent().equals(choice.getChoiceContent()))
                    .findFirst()
                    .orElse(null);

            if (existingChoice != null) {
                existingChoice.setChoiceKey(choice.isChoiceKey());
                questionChoiceRepository.save(existingChoice);
            } else {
                Question_Choice newChoice = new Question_Choice();
                newChoice.setChoiceContent(choice.getChoiceContent());
                newChoice.setChoiceKey(choice.isChoiceKey());
                newChoice.setQuestion(question);
                questionChoiceRepository.save(newChoice);
            }
        }

        questionDTO.setId(updatedQuestion.getId());
        return questionDTO;
    }

    public void deleteQuestion(Long id) {
        // Xóa tất cả các lựa chọn liên quan đến câu hỏi
        List<Question_Choice> choices = questionChoiceRepository.findByQuestionId(id);
        questionChoiceRepository.deleteAll(choices);

        // Xóa câu hỏi
        questionRepository.deleteById(id);
    }

    public Page<Question> getAllQuestions(Specification<Question> spec, Pageable pageable) {
        return questionRepository.findAll(spec, pageable);
    }
}