package com.adaptive.quiz.controller;

import com.adaptive.quiz.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    public static final String SESSION_REGISTER = "register";
    private final RegisterService registerService;

    @PostMapping(value = "/quiz/register")
    public String register(@RequestParam(name = "name") String name, Model model, HttpServletRequest request) {

        RegisterResponse response = registerService.register(name);
        request.getSession(true).setAttribute(SESSION_REGISTER, response);
        return "home";
    }


}
