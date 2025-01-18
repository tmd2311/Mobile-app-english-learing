package utc.englishlearning.Encybara.domain.response.lesson;

import lombok.Getter;
import lombok.Setter;
import utc.englishlearning.Encybara.util.constant.SkillTypeEnum;

import java.time.Instant;

@Getter
@Setter
public class ResLessonDTO {
    private long id;
    private String name;
    private SkillTypeEnum skillType;
    private String createBy;
    private Instant createAt;
    private String updateBy;
    private Instant updateAt;
}