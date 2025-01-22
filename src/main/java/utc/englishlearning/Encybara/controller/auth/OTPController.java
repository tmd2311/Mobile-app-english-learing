package utc.englishlearning.Encybara.controller.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utc.englishlearning.Encybara.domain.User;
import utc.englishlearning.Encybara.domain.request.ErrorResponseDTO;
import utc.englishlearning.Encybara.domain.auth.request.OtpVerificationRequest;
import utc.englishlearning.Encybara.domain.auth.reponse.RegisterReponseDTO;
import utc.englishlearning.Encybara.domain.auth.reponse.OtpVerifyResponse;
import utc.englishlearning.Encybara.domain.auth.reponse.ResCreateUserDTO;
import utc.englishlearning.Encybara.service.EmailService;
import utc.englishlearning.Encybara.service.OtpService;
import utc.englishlearning.Encybara.service.UserService;
import utc.englishlearning.Encybara.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1/otp/")
public class OTPController {
    private final OtpService otpService;
    private final EmailService emailService;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;



    public OTPController(PasswordEncoder passwordEncoder, OtpService otpService, EmailService emailService, SecurityUtil securityUtil, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    // API gửi lại otp dựa vào id
    // {
    // "otpID": "B758E8"
    // }
    @PostMapping("/resend-otp")
    public ResponseEntity<RegisterReponseDTO> resendOTP(
            @Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        String otpID = otpVerificationRequest.getOtpID();

        OtpVerificationRequest temp = otpService.getOtpData(otpID);
        String email = temp.getEmail();
        String newOTP = otpService.generateOtp(email);
        otpService.updateOtp(otpID, newOTP);

        // Gui email lại
        emailService.sendEmailFromTemplateSync(temp.getUserDTO().getEmail(), "Your OTP Code", newOTP);
        RegisterReponseDTO registerReponseDTO = new RegisterReponseDTO(
                "A new OTP has been sent to your email. Please verify to complete registration.",
                otpID,
                "Expires in 5 minutes");
        return ResponseEntity.ok(registerReponseDTO);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody OtpVerificationRequest otpRequest){
        if(!otpService.validateOtp(otpRequest.getOtpID(), otpRequest.getOtp())){
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
        OtpVerificationRequest storedData = otpService.getOtpData(otpRequest.getOtpID());
        if(storedData == null){
            return ResponseEntity.badRequest().body("Data not found");
        }
        switch (storedData.getType()){
            case "forgotpassword":
                return handleForgotPassword(storedData);
            case "register":
                return handleRegistration(storedData);
            default:
                return ResponseEntity.badRequest().body("Invalid type");
        }
    }

    private ResponseEntity<?> handleForgotPassword(OtpVerificationRequest otpRequest) {
        try{
            String mail = otpRequest.getEmail();

            // tạo token để reset mật khẩu
            String restToken = securityUtil.creatResetPasswordToken(mail);
            otpService.removeOtpData(otpRequest.getOtpID());
            return ResponseEntity.ok(new OtpVerifyResponse("OTP verified successfully", restToken));
        }
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating token: " +e.getMessage());
        }
    }
    private ResponseEntity<?> handleRegistration(OtpVerificationRequest otpRequest) {
        try {
            if (!otpService.validateOtp(otpRequest.getOtpID(), otpRequest.getOtp())) { // ham validate viet nguoc de xoa nen de y
                return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid or expired OTP"));
            }

            OtpVerificationRequest storedData = otpService.getOtpData(otpRequest.getOtpID());
            if (storedData == null) {
                return ResponseEntity.badRequest().body(new ErrorResponseDTO("Registration data not found"));
            }
            // Lấy thông tin người đăng ký để chuẩn bị lưu vào db
            ResCreateUserDTO resCreateUserDTO = storedData.getUserDTO();

            // Mã hoa mật khẩu và lưu vào db
            String encodedPassword = passwordEncoder.encode(resCreateUserDTO.getPassword());
            User user = new User();
            user.setName(resCreateUserDTO.getName());
            user.setEmail(resCreateUserDTO.getEmail());
            user.setPassword(encodedPassword);

            // Lưu người dùng vào cơ sở dữ liệu
            userService.handleCreateUser(user);
            otpService.removeOtpData(otpRequest.getOtpID());
            return ResponseEntity.ok(new ErrorResponseDTO("Registration successful"));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Error during registration: " + e.getMessage()));
        }

    }

}
