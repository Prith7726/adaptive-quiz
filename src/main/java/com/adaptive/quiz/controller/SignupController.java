package com.adaptive.quiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    @RequestMapping("/signup/addNewUser")
    public String addNewUser(
            String username,
            String password) {

        jdbcUserDetailsManager.createUser(
                User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .authorities("USER")
                        .build());

        return "redirect:/login";
    }
}
