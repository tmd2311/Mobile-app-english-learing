package utc.englishlearning.Encybara.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;

    private String phone;
    private int field;
    private String avatar;
    private String englishlevel;

}
