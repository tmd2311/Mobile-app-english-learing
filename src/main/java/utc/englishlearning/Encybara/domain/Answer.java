package utc.englishlearning.Encybara.domain;

import java.time.Instant;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import utc.englishlearning.Encybara.util.SecurityUtil;

@Entity
@Table(name = "answers")
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Choice_ID
    private int point_achieved;
    private String createBy;
    private Instant createAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createAt = Instant.now();
    }

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    private List<Answer_Choice> answerChoices;

    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    private Answer_Text answerText;

    @OneToOne(mappedBy = "answer", fetch = FetchType.LAZY)
    private Answer_Voice answerVoice;

}