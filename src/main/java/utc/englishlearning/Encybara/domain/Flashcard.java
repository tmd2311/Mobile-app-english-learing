package utc.englishlearning.Encybara.domain;

import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "flashcards")
@Getter
@Setter
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String word;
    private boolean learnedStatus;
    private Instant addedDate;
    private String example;
    private String definitions;
    private String vietNameseMeaning;
    private Instant lastReviewed;

    @ManyToOne
    @JoinColumn(name = "flashcard_group_id")
    private FlashcardGroup flashcardGroup;

    public String getVietNameseMeaning() {
        return vietNameseMeaning;
    }

    public void setVietNameseMeaning(String vietNameseMeaning) {
        this.vietNameseMeaning = vietNameseMeaning;
    }
}
