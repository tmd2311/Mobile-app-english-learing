package utc.englishlearning.Encybara.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.englishlearning.Encybara.domain.Discussion;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByLessonId(Long lessonId);

    // Trong DiscussionRepository.java
    List<Discussion> findByParentDiscussionId(Long parentId);
}