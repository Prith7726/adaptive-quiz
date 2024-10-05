package com.adaptive.quiz.controller;

import com.adaptive.quiz.service.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping(value = "/quiz/start")
    public String register(HttpServletRequest request, Model model) {

        model.addAttribute("query", quizService.getQuery());
        return "quiz";
    }

    @PostMapping(value = "/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = "currentIndex") String currentIndex, Model model) {
        model.addAttribute("query", quizService.getQuery(Integer.valueOf(currentIndex)));
        model.addAttribute("previous", currentIndex);
        return "quiz";
    }
}
