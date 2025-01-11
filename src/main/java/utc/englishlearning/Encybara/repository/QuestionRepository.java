package utc.englishlearning.Encybara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utc.englishlearning.Encybara.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Additional query methods can be defined here
}