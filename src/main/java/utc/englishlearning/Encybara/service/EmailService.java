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
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("englishlearning");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        this.mailSender.send(message);
        System.out.println("Sending email to " + to + " with body: " + text);
    }
}
