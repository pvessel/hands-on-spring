package com.handsonspring.service;

import com.handsonspring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    private EmailService emailService;

    @BeforeEach
    void initUseCase() {
        emailService = new EmailService(emailSender);
    }

    @Test
    void updatePassword_whenCalled_thenUserUpdate() {
        // given
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("email@email.com");
        String resetUrl = "http://reset-url.com";
        MimeMessage message = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(message);

        // when
        emailService.sendPasswordRequestMail(resetUrl, user);

        // then
        Mockito.verify(emailSender, times(1)).send(any(MimeMessage.class));
    }
}