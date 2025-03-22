package com.adaptive.quiz.controller;


import com.adaptive.quiz.service.QuizListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final QuizListService quizListService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("quizList", quizListService.getAllQuiz());
        return "home";
    }
}
