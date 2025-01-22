package utc.englishlearning.Encybara.domain.auth.reponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReponseDTO {
    private  String message;
    private String otpID;
    private String timestamp;

    public RegisterReponseDTO(String message, String otpID, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
        this.otpID = otpID;
    }
}
