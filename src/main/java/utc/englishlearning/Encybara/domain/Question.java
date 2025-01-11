package utc.englishlearning.Encybara.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import utc.englishlearning.Encybara.util.constant.QuestionTypeEnum;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import java.util.List;
import jakarta.persistence.CascadeType;

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
    private int point;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Lesson_Question> lessonQuestions;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Question_Choice> questionChoices;

    @OneToOne(mappedBy = "question", fetch = FetchType.LAZY)
    private Learning_Material learningMaterial;

    @OneToOne(mappedBy = "question", fetch = FetchType.LAZY)
    private Skill skill;
}