package utc.englishlearning.Encybara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utc.englishlearning.Encybara.domain.Question_Choice;

public interface QuestionChoiceRepository extends JpaRepository<Question_Choice, Long> {
    // Bạn có thể định nghĩa thêm các phương thức truy vấn nếu cần
}