package utc.englishlearning.Encybara.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import utc.englishlearning.Encybara.domain.RestResponse;
import utc.englishlearning.Encybara.domain.User;
import utc.englishlearning.Encybara.domain.dto.ErrorResponseDTO;
import utc.englishlearning.Encybara.domain.dto.OtpVerificationRequest;
import utc.englishlearning.Encybara.domain.dto.RegisterDTO;
import utc.englishlearning.Encybara.domain.dto.ResLoginDTO;
import utc.englishlearning.Encybara.service.EmailService;
import utc.englishlearning.Encybara.service.OtpService;
import utc.englishlearning.Encybara.service.UserService;
import org.springframework.http.HttpStatus;
import utc.englishlearning.Encybara.util.error.IdInvalidException;

@RestController
public class RegisterController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    public RegisterController(UserService userService, PasswordEncoder passwordEncoder
    , OtpService otpService, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<ErrorResponseDTO> register(@Valid @RequestBody RegisterDTO registerDto)  {
        // Kiểm tra xem email đã tồn tại chưa
//        if (userService.existsByEmail(registerDto.getEmail())) {
//            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Email already exists"));
//        }
        String otp = otpService.generateOtp(registerDto.getEmail());
        emailService.sendEmailFromTemplateSync(registerDto.getEmail(), "Your OTP Code",  otp);
        otpService.saveRegisterData(registerDto.getEmail(), registerDto);

        return ResponseEntity.ok(new ErrorResponseDTO("OTP sent to your email. Please verify to complete registration."));
    }

    //Xác thực OTP ở đây
    @PostMapping("/verify-otp")
    public ResponseEntity<ErrorResponseDTO> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpRequest) {
        if(!otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp())) {
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid OTP"));
        }
        RegisterDTO registerDto = otpService.getRegisterData(otpRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(encodedPassword);

        // Lưu người dùng vào cơ sở dữ liệu
        userService.handleCreateUser(user);
        return ResponseEntity.ok(new ErrorResponseDTO("Registration successful"));
    }
}