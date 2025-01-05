package utc.englishlearning.Encybara.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController 
public class HomeController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Trả về view login.jsp mà không cần phần mở rộng
    }
}
