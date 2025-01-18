package utc.englishlearning.Encybara.domain.request.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateCourseDTO {
    private String name;
    private String intro;
    private int diffLevel;
    private int recomLevel;
    private String courseType;
    private String speciField;
}