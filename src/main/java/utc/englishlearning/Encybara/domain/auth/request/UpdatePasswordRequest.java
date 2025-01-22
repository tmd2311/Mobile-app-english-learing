package utc.englishlearning.Encybara.domain.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    String resetToken;
    String newPassword;
    String confirmPassword;


}
