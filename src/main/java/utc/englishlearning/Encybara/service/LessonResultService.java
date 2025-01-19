package utc.englishlearning.Encybara.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utc.englishlearning.Encybara.domain.Answer;
import utc.englishlearning.Encybara.domain.Lesson;
import utc.englishlearning.Encybara.domain.Lesson_Result;
import utc.englishlearning.Encybara.domain.Question;
import utc.englishlearning.Encybara.domain.User;
import utc.englishlearning.Encybara.domain.request.lesson.ReqCreateLessonResultDTO;
import utc.englishlearning.Encybara.domain.response.lesson.ResLessonResultDTO;
import utc.englishlearning.Encybara.exception.ResourceNotFoundException;
import utc.englishlearning.Encybara.repository.LessonResultRepository;
import utc.englishlearning.Encybara.repository.AnswerRepository;
import utc.englishlearning.Encybara.repository.QuestionRepository;
import utc.englishlearning.Encybara.repository.UserRepository;
import utc.englishlearning.Encybara.repository.LessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import utc.englishlearning.Encybara.util.SecurityUtil;

import java.util.List;

@Service
public class LessonResultService {

    @Autowired
    private LessonResultRepository lessonResultRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public ResLessonResultDTO createLessonResult(ReqCreateLessonResultDTO reqDto) {
//        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserLogin()
//                .orElseThrow(() -> new RuntimeException("User not authenticated")));
        User user = userRepository.findById(reqDto.getUserId()).get();

        Lesson lesson = lessonRepository.findById(reqDto.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        List<Question> questions = questionRepository.findByLesson(lesson);

        List<Answer> answers = answerRepository.findByUserAndQuestionInAndSessionId(user, questions,
                reqDto.getSessionId());
        int totalPointsAchieved = answers.stream().mapToInt(Answer::getPoint_achieved).sum();

        int totalPointsPossible = questionRepository.findByLesson(lesson)
                .stream().mapToInt(Question::getPoint).sum();

        double comLevel = totalPointsPossible > 0 ? (double) totalPointsAchieved / totalPointsPossible * 100 : 0;

        Lesson_Result lessonResult = new Lesson_Result();
        lessonResult.setLesson(lesson);
        lessonResult.setUser(user);
        lessonResult.setSessionId(reqDto.getSessionId());
        lessonResult.setStuTime(reqDto.getStuTime());
        lessonResult.setTotalPoints(totalPointsAchieved);
        lessonResultRepository.save(lessonResult);

        ResLessonResultDTO resDto = new ResLessonResultDTO();
        resDto.setId(lessonResult.getId());
        resDto.setLessonId(lesson.getId());
        resDto.setUserId(user.getId());
        resDto.setSessionId(reqDto.getSessionId());
        resDto.setStuTime(reqDto.getStuTime());
        resDto.setPoint(totalPointsAchieved);
        resDto.setComLevel(comLevel);

        return resDto;
    }

    public Page<Lesson_Result> getResultsByLessonId(Long lessonId, Pageable pageable) {
        return lessonResultRepository.findByLessonId(lessonId, pageable);
    }

    public Page<Lesson_Result> getResultsByUserIdAndLessonId(Long userId, Long lessonId, Pageable pageable) {
        return lessonResultRepository.findByUserIdAndLessonId(userId, lessonId, pageable);
    }

    public Page<Lesson_Result> getLatestResultsByUserId(Long userId, Pageable pageable) {
        return lessonResultRepository.findByUserIdOrderBySessionIdDesc(userId, pageable);
    }

    public List<Lesson_Result> getLatestResultsByUserIdAndLessonId(Long userId, Long lessonId) {
        return lessonResultRepository.findByUserIdAndLessonIdOrderBySessionIdDesc(userId, lessonId);
    }
}