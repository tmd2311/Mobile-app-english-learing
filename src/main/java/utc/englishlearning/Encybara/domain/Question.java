package utc.englishlearning.Encybara.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
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
    private String quesType;
    private int point;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Lesson_Question> lessonQuestions;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Question_Choice> questionChoices;

    @OneToOne(mappedBy = "question",fetch = FetchType.LAZY)
    private Learning_Material learningMaterial;

    @OneToOne(mappedBy = "question",fetch = FetchType.LAZY)
    private Skill skill;
}