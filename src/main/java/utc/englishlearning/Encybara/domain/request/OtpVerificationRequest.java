package utc.englishlearning.Encybara.domain.request;

import lombok.Getter;
import lombok.Setter;
import utc.englishlearning.Encybara.domain.response.ResCreateUserDTO;

@Getter
@Setter
public class OtpVerificationRequest {
    private  String otpID;
    private String email;
    private String otp;
    private ResCreateUserDTO userDTO;
    private long timestamp; // Thời gian tạo OTP

    public OtpVerificationRequest(String otpID, String otp, String email, ResCreateUserDTO userDTO, long timestamp) {
        this.otpID = otpID;
        this.email = email;
        this.otp = otp;
        this.userDTO = userDTO;
        this.timestamp = timestamp;
    }
}
