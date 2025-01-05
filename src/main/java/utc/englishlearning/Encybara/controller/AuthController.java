package utc.englishlearning.Encybara.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import utc.englishlearning.Encybara.domain.User;
import utc.englishlearning.Encybara.domain.dto.LoginDTO;
import utc.englishlearning.Encybara.domain.dto.RegisterDTO;
import utc.englishlearning.Encybara.domain.dto.ResLoginDTO;
import utc.englishlearning.Encybara.service.UserService;
import utc.englishlearning.Encybara.util.SecurityUtil;

@RestController
public class AuthController {
    private UserService userService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          SecurityUtil securityUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create a token
        String access_token = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);
        return ResponseEntity.ok().body(res);
    }
    @PostMapping("/register")
    public ResponseEntity<ResLoginDTO> register(@Valid @RequestBody RegisterDTO registerDto) {
        if(userService.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body(null);
        }

        // Mã hóa mật khẩu người dùng
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // Tạo đối tượng User từ thông tin đăng ký
        User user = new User();
        user.setEmail(registerDto.getEmail());  // Đảm bảo có email
        user.setPassword(encodedPassword);

        // Lưu người dùng vào cơ sở dữ liệu
        User savedUser = userService.handleCreateUser(user);

        // Tạo Authentication từ User
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());

        // Tạo token từ Authentication
        String accessToken = securityUtil.createToken(authentication);

        // Tạo đối tượng ResLoginDTO và trả về access token
        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(accessToken);

        return ResponseEntity.ok().body(res);
    }

}