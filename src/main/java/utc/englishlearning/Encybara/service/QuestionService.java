package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utc.englishlearning.Encybara.domain.Question;
import utc.englishlearning.Encybara.repository.QuestionRepository;
import utc.englishlearning.Encybara.domain.Question_Choice;
import utc.englishlearning.Encybara.repository.QuestionChoiceRepository;
import utc.englishlearning.Encybara.domain.response.question.ResCreateQuestionDTO;
import utc.englishlearning.Encybara.domain.response.question.ResUpdateQuestionDTO;

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

    public ResUpdateQuestionDTO updateQuestion(Long id, ResUpdateQuestionDTO questionDTO) {
        Question question = new Question();
        question.setId(id);
        question.setQuesContent(questionDTO.getQuesContent());
        question.setKeyword(questionDTO.getKeyword());
        question.setQuesType(questionDTO.getQuesType());
        question.setPoint(questionDTO.getPoint());
        // Cập nhật câu hỏi
        Question updatedQuestion = questionRepository.save(question);
        // Chuyển đổi lại thành DTO để trả về
        questionDTO.setId(updatedQuestion.getId());
        return questionDTO;
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}