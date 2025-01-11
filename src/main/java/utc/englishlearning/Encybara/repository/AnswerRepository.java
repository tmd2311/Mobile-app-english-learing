package utc.englishlearning.Encybara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utc.englishlearning.Encybara.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // Additional query methods can be defined here
}