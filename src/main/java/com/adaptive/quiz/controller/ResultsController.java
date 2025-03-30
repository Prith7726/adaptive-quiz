package com.adaptive.quiz.controller;

import com.adaptive.quiz.model.Result;
import com.adaptive.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResultsController {

    private final QuizService quizService;

    @RequestMapping("/v1/quiz/results")
    public String quizResults(Model model) {
        List<Result> results = quizService.findLastFiveResults();
        model.addAttribute("resultsFound", !results.isEmpty());
        model.addAttribute("results", results);
        return "results";
    }
}
