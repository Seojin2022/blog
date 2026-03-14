package portfolio.myweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import portfolio.myweb.dto.ContactRequest;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.contact.to}")
    private String contactTo;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendContactMail(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(contactTo);
        message.setFrom(fromEmail);
        message.setReplyTo(request.getEmail());
        message.setSubject("[Portfolio Contact] " + request.getSubject());
        message.setText(
                "이름: " + request.getName() + "\n" +
                        "이메일: " + request.getEmail() + "\n\n" +
                        "내용:\n" + request.getMessage()
        );

        mailSender.send(message);
    }
}