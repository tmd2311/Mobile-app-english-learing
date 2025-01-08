package utc.englishlearning.Encybara.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "answer_choices")
@Getter
@Setter
public class Answer_Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Choice_ID

    private String choiceContent; // Choice_Content

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer; 
}
