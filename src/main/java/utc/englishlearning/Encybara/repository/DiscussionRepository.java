package utc.englishlearning.Encybara.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.englishlearning.Encybara.domain.Discussion;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByLessonId(Long lessonId);

    // Trong DiscussionRepository.java
    List<Discussion> findByParentDiscussionId(Long parentId);

    Page<Discussion> findByLessonId(Long lessonId, Pageable pageable);

    List<Discussion> findByUserId(Long userId);

    Page<Discussion> findByUserId(Long userId, Pageable pageable);
}