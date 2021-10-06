package com.handsonspring.web;

import com.handsonspring.service.EmailService;
import com.handsonspring.model.User;
import com.handsonspring.model.security.PasswordResetToken;
import com.handsonspring.repository.PasswordResetTokenRepository;
import com.handsonspring.service.UserService;
import com.handsonspring.validator.PasswordForgotDto;
import com.handsonspring.validator.PasswordResetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class PasswordResetController {
    private UserService userService;

    private final PasswordResetTokenRepository tokenRepository;

    private final EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetController(UserService userService, PasswordResetTokenRepository tokenRepository, EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }

    @RequestMapping(value = {"/forgot-password"}, method = RequestMethod.GET)
    public String displayForgotPasswordPage(Model model, String success) {

        if (success != null) {
            model.addAttribute("message", "Further instructions has been sent by email.");
        }

        return "forgot-password";
    }


    @RequestMapping(value = {"/forgot-password"}, method = RequestMethod.POST)
    public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
                                            BindingResult result,
                                            HttpServletRequest request) {

        if (result.hasErrors()){
            return "forgot-password";
        }

        User user = userService.findByEmail(form.getEmail());
        if (user == null){
            result.rejectValue("email", null, "We could not find an account for that e-mail address.");
            return "forgot-password";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);

        String resetUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/reset-password?token=" + token.getToken();
        emailService.sendPasswordRequestMail(resetUrl, user);

        return "redirect:/forgot-password?success";
    }

    @RequestMapping(value = {"/reset-password"}, method = RequestMethod.GET)
    public String displayResetPasswordPage(@RequestParam(required = false) String token,
                                           Model model) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null){
            model.addAttribute("error", "Could not find password reset token.");
        } else if (resetToken.isExpired()){
            model.addAttribute("error", "Token has expired, please request a new password reset.");
        } else {
            model.addAttribute("token", resetToken.getToken());
        }
        model.addAttribute("passwordResetForm", new PasswordResetDto());

        return "reset-password";
    }


    @RequestMapping(value = {"/reset-password"}, method = RequestMethod.POST)
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetDto form,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", form);
            return "redirect:/reset-password?token=" + form.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(form.getToken());
        User user = token.getUser();
        String updatedPassword = passwordEncoder.encode(form.getPassword());
        userService.updatePassword(updatedPassword, user.getId());
        tokenRepository.delete(token);

        return "redirect:/reset-password-success";
    }

    @RequestMapping(value = "/reset-password-success", method = RequestMethod.GET)
    public String resetPasswordSuccess(Model model) {

        model.addAttribute("message", "You have changed password successfully.");

        return "login";
    }
}
