package utc.englishlearning.Encybara.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import utc.englishlearning.Encybara.domain.Flashcard;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long>, JpaSpecificationExecutor<Flashcard> {

    @Query("SELECT f FROM Flashcard f WHERE f.flashcardGroup.id = :groupId " +
            "AND (:word IS NULL OR f.word LIKE %:word%) " +
            "AND (:learnedStatus IS NULL OR f.learnedStatus = :learnedStatus) " +
            "AND (:vietnameseMeaning IS NULL OR f.vietNameseMeaning LIKE %:vietnameseMeaning%)")
    Page<Flashcard> findAllByGroupIdAndFilters(@Param("groupId") Long groupId,
            @Param("word") String word,
            @Param("learnedStatus") Boolean learnedStatus,
            @Param("vietnameseMeaning") String vietnameseMeaning,
            Pageable pageable);

    List<Flashcard> findAllByOrderByLastReviewedDesc();

    List<Flashcard> findAllByOrderByLastReviewedAsc();
}