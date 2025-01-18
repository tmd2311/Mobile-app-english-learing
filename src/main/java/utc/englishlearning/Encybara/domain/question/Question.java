package utc.englishlearning.Encybara.domain.question;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import utc.englishlearning.Encybara.util.constant.QuestionTypeEnum;
import utc.englishlearning.Encybara.util.constant.SkillTypeEnum;
import utc.englishlearning.Encybara.util.SecurityUtil;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String quesContent;
    private String keyword;
    private QuestionTypeEnum quesType;
    private SkillTypeEnum skillType;
    private int point;

    private String createBy;
    private Instant createAt;
    private String updateBy;
    private Instant updateAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.createAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updateBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.updateAt = Instant.now();
    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<QuestionChoice> questionChoices;
} 