package utc.englishlearning.Encybara.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final MailSender mailSender;
    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("englishlearning");
        message.setTo("truongluu1888@gmail.com");
        message.setSubject("Test from englishlearning");
        message.setText("https://www.facebook.com/tmd.23.11/");
        this.mailSender.send(message);
    }
}
