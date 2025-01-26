package utc.englishlearning.Encybara.domain.response.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReponseDTO {
    private String otpID;
    private String timestamp;

    public RegisterReponseDTO(String otpID, String timestamp) {
        this.timestamp = timestamp;
        this.otpID = otpID;
    }
}
