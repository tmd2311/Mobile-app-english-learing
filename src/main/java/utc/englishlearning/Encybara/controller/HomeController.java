package utc.englishlearning.Encybara.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Hello World";
    }
    //return login view
    @GetMapping("/login")
    public String loginView() {return "guest/login";}

    //return register view
    @GetMapping("/register")
    public String registerView() {return "guest/register";}



}

