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
import utc.englishlearning.Encybara.domain.dto.RegisterDTO;
import utc.englishlearning.Encybara.domain.dto.ResLoginDTO;
import utc.englishlearning.Encybara.service.UserService;
import org.springframework.http.HttpStatus;
import utc.englishlearning.Encybara.util.error.IdInvalidException;

@RestController
public class RegisterController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ErrorResponseDTO> register(@Valid @RequestBody RegisterDTO registerDto)  {
        // Kiểm tra xem email đã tồn tại chưa
        if (userService.existsByEmail(registerDto.getEmail())) {
            ErrorResponseDTO erro = new ErrorResponseDTO("Email already exists");
            return ResponseEntity.badRequest().body(erro);
        }

        // Mã hóa mật khẩu người dùng
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // Tạo đối tượng User từ thông tin đăng ký
        User user = new User();
        user.setEmail(registerDto.getEmail()); // Đảm bảo có email
        user.setPassword(encodedPassword);

        // Lưu người dùng vào cơ sở dữ liệu
        User savedUser = userService.handleCreateUser(user);
        ErrorResponseDTO erro = new ErrorResponseDTO("Register successful");
        return ResponseEntity.status(HttpStatus.CREATED).body(erro);
    }
}