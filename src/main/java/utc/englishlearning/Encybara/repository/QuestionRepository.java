package utc.englishlearning.Encybara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import utc.englishlearning.Encybara.domain.Lesson;
import utc.englishlearning.Encybara.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    // Additional query methods can be defined here

    List<Question> findByLessonId(Long lessonId);

    List<Question> findByLesson(Lesson lesson);
}