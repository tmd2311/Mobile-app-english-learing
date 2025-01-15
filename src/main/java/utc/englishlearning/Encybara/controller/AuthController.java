package utc.englishlearning.Encybara.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import utc.englishlearning.Encybara.domain.User;
import utc.englishlearning.Encybara.domain.request.OtpVerificationRequest;
import utc.englishlearning.Encybara.domain.request.RegisterReponseDTO;
import utc.englishlearning.Encybara.domain.request.ReqLoginDTO;
import utc.englishlearning.Encybara.domain.response.ResLoginDTO;
import utc.englishlearning.Encybara.domain.response.ResCreateUserDTO;
import utc.englishlearning.Encybara.domain.request.ErrorResponseDTO;
import utc.englishlearning.Encybara.service.EmailService;
import utc.englishlearning.Encybara.service.OtpService;
import utc.englishlearning.Encybara.service.UserService;
import utc.englishlearning.Encybara.util.SecurityUtil;
import utc.englishlearning.Encybara.util.error.IdInvalidException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;
        private final OtpService otpService;
        private final EmailService emailService;

        @Value("${englishlearning.jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                        SecurityUtil securityUtil,
                        UserService userService,
                        PasswordEncoder passwordEncoder, OtpService otpService, EmailService emailService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
                this.passwordEncoder = passwordEncoder;
                this.otpService = otpService;
                this.emailService = emailService;
        }

        @PostMapping("/login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(), loginDto.getPassword());

                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                ResLoginDTO res = new ResLoginDTO();
                User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
                if (currentUserDB != null) {
                        res.setUser(new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getName()));
                }

                String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
                res.setAccessToken(access_token);

                String refresh_token = this.securityUtil.createRefreshToken(loginDto.getUsername(), res);
                this.userService.updateUserToken(refresh_token, loginDto.getUsername());

                ResponseCookie resCookies = ResponseCookie
                                .from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(res);
        }

        @GetMapping("/account")
        public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
                String email = SecurityUtil.getCurrentUserLogin().orElse("");

                User currentUserDB = this.userService.handleGetUserByUsername(email);
                ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

                if (currentUserDB != null) {
                        userGetAccount.setUser(new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getName()));
                }

                return ResponseEntity.ok().body(userGetAccount);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout() throws IdInvalidException {
                String email = SecurityUtil.getCurrentUserLogin().orElse("");

                if (email.isEmpty()) {
                        throw new IdInvalidException("Access Token không hợp lệ");
                }

                this.userService.updateUserToken(null, email);

                ResponseCookie deleteSpringCookie = ResponseCookie
                                .from("refresh_token", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                                .body(null);
        }



        @PostMapping("/register")
        public ResponseEntity<RegisterReponseDTO> register(@Valid @RequestBody ResCreateUserDTO resCreateUserDto) {
                // Kiểm tra xem email đã tồn tại chưa
//                 if (userService.isEmailExist(resCreateUserDto.getEmail())) {
//                        return ResponseEntity.badRequest().body("Email already exists");
//                 }
                String otp = otpService.generateOtp(resCreateUserDto.getEmail());


                //off dong nay test api cho nhanh
                emailService.sendEmailFromTemplateSync(resCreateUserDto.getEmail(), "Your OTP Code", otp);

                String otpID =otpService.saveRegisterData(resCreateUserDto.getEmail(), resCreateUserDto, otp);
                RegisterReponseDTO registerReponseDTO = new RegisterReponseDTO(
                        "OTP sent to your email. Please verify to complete registration.",
                        otpID,
                        "Expires in 5 minutes"
                );
                return ResponseEntity.ok(registerReponseDTO);
        }


        //API gửi lại otp dựa vào id
//{
//    "otpID": "B758E8"
//}
        @PostMapping("/resend-otp")
        public ResponseEntity<RegisterReponseDTO> resendOTP(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
                String otpID = otpVerificationRequest.getOtpID();

                OtpVerificationRequest temp = otpService.getOtpData(otpID);
                String email = temp.getEmail();
                String newOTP= otpService.generateOtp(email);
                otpService.updateOtp(otpID, newOTP);

                //Gui email lại
                emailService.sendEmailFromTemplateSync(temp.getUserDTO().getEmail(), "Your OTP Code", newOTP);
                RegisterReponseDTO registerReponseDTO = new RegisterReponseDTO(
                        "A new OTP has been sent to your email. Please verify to complete registration.",
                        otpID,
                        "Expires in 5 minutes"
                );
                return ResponseEntity.ok(registerReponseDTO);

        }


        // Xác thực OTP ở đây
        @PostMapping("/verify-otp")
        public ResponseEntity<ErrorResponseDTO> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpRequest) {

                //check thong tin
                if (!otpService.validateOtp(otpRequest.getOtpID(), otpRequest.getOtp())) {   // ham validate viet nguoc de xoa nen de y
                        return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid or expired OTP"));
                }
                OtpVerificationRequest storedData = otpService.getOtpData(otpRequest.getOtpID());
                if(storedData == null) {
                        return ResponseEntity.badRequest().body(new ErrorResponseDTO("Registration data not found"));
                }
                //Lấy thông tin người đăng ký để chuẩn bị lưu vào db
                ResCreateUserDTO resCreateUserDTO = storedData.getUserDTO();

                //Mã hoa mật khẩu và lưu vào db
                String encodedPassword = passwordEncoder.encode(resCreateUserDTO.getPassword());
                User user = new User();
                user.setName(resCreateUserDTO.getName());
                user.setEmail(resCreateUserDTO.getEmail());
                user.setPassword(encodedPassword);

                // Lưu người dùng vào cơ sở dữ liệu
                userService.handleCreateUser(user);
                otpService.removeOtpData(otpRequest.getOtpID());
                return ResponseEntity.ok(new ErrorResponseDTO("Registration successful"));
        }
}