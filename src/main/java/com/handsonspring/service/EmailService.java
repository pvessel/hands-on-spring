package com.handsonspring.service;

import com.handsonspring.model.Mail;
import com.handsonspring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendPasswordRequestMail(String resetUrl, User user) {

        Mail mail = new Mail();
        mail.setFrom("email@email.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String html = "You've requested a password reset.\n<br />" +
                    "Use this link: " + resetUrl + "\n\n<br /><br />" +
                    "Best Regards \n<br />" +
                    "Author";
            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            emailSender.send(message);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}