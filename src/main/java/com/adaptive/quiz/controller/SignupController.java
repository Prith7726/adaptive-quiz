package com.adaptive.quiz.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupController {
    public static final String SIGNUP = "signup";
    public static final String ERROR_MESSAGE = "errorMessage";
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    @RequestMapping("/signup/addNewUser")
    public String addNewUser(
            String username,
            String password,
            Model model) {

        if (!StringUtils.hasText(username)) {
            model.addAttribute(ERROR_MESSAGE, "Username cannot be empty!");
            return SIGNUP;
        }
        if (!StringUtils.hasText(password)) {
            model.addAttribute(ERROR_MESSAGE, "Password cannot be empty!");
            return SIGNUP;
        }
        try {
            jdbcUserDetailsManager.createUser(
                    User.builder()
                            .username(username)
                            .password(passwordEncoder.encode(password))
                            .authorities("USER")
                            .build());
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Username exists!");
            return SIGNUP;
        }
        return "redirect:/login?signupSuccess=true";
    }
}
