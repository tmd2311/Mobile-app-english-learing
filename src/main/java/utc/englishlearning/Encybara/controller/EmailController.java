package utc.englishlearning.Encybara.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utc.englishlearning.Encybara.service.EmailService;

@RestController
public class EmailController {
    private final EmailService emailService;
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @RequestMapping("/email")
    public String email() {
        return "Send email successfully";
    }
}
