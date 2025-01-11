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
import utc.englishlearning.Encybara.domain.request.ReqLoginDTO;
import utc.englishlearning.Encybara.domain.response.ResLoginDTO;
import utc.englishlearning.Encybara.service.UserService;
import utc.englishlearning.Encybara.util.SecurityUtil;
import utc.englishlearning.Encybara.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        @Value("${englishlearning.jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                        SecurityUtil securityUtil,
                        UserService userService,
                        PasswordEncoder passwordEncoder) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
                this.passwordEncoder = passwordEncoder;
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
        public ResponseEntity<ResLoginDTO> register(@Valid @RequestBody User postManUser) throws IdInvalidException {
                boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
                if (isEmailExist) {
                        throw new IdInvalidException(
                                        "Email " + postManUser.getEmail()
                                                        + " đã tồn tại, vui lòng sử dụng email khác.");
                }

                String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
                postManUser.setPassword(hashPassword);
                User savedUser = this.userService.handleCreateUser(postManUser);

                ResLoginDTO res = new ResLoginDTO();
                res.setUser(new ResLoginDTO.UserLogin(
                                savedUser.getId(),
                                savedUser.getEmail(),
                                savedUser.getName()));

                return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
}