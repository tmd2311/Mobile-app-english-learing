package utc.englishlearning.Encybara.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "flashcard_groups")
@Getter
@Setter
public class FlashcardGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "flashcardGroup", fetch = FetchType.LAZY)
    private List<Flashcard> flashcards;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}