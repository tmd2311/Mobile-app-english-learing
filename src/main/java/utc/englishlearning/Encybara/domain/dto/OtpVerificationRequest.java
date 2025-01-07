package utc.englishlearning.Encybara.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
