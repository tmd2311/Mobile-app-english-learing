package utc.englishlearning.Encybara.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
    private String name;
    private String email;
    private String password;
}
