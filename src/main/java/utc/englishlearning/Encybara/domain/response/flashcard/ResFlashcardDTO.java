package utc.englishlearning.Encybara.domain.response.flashcard;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResFlashcardDTO {
    private long id;
    private String word;
    private String example;
    private String definitions;
    private String VietNameseMeaning;
    private Instant addedDate;
    private boolean learnedStatus;
    private Instant lastReviewed;

    public Instant getLastReviewed() {
        return lastReviewed;
    }

    public void setLastReviewed(Instant lastReviewed) {
        this.lastReviewed = lastReviewed;
    }
}