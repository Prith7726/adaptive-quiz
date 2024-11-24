package com.example.securingweb.controller;

import com.example.securingweb.model.NewUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {
    @GetMapping("/signup")
    public String signup(Model model) {
        NewUser user = new NewUser();
        model.addAttribute("newUser", user);
        return "signup";
    }
}
