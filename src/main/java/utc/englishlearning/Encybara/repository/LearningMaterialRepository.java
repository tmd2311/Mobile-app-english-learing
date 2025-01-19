package utc.englishlearning.Encybara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utc.englishlearning.Encybara.domain.Learning_Material;

import java.util.Optional;

public interface LearningMaterialRepository extends JpaRepository<Learning_Material, Long> {
    // Bạn có thể định nghĩa thêm các phương thức truy vấn nếu cần
    Optional<Learning_Material> findByQuestionId(Long questionId);

}