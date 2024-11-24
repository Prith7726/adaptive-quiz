package com.adaptive.quiz.controller;

import com.adaptive.quiz.service.QuizService;
import com.adaptive.quiz.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QuizController {
    public static final String SESSION_REGISTER = "register";
    public static final String QUERY = "query";
    public static final String CURRENT_INDEX = "currentIndex";
    public static final String HAS_NEXT = "hasNext";
    public static final String HAS_PREVIOUS = "hasPrevious";
    private final QuizService quizService;
    private final RegisterService registerService;

    @PostMapping(value = "/quiz/start")
    public String start(@RequestParam(name = "name") String name, Model model, HttpServletRequest request) {
        RegisterResponse response = registerService.register(name);
        request.getSession(true).setAttribute(SESSION_REGISTER, response);

        model.addAttribute(QUERY, quizService.getQuery());
        model.addAttribute(CURRENT_INDEX, 0);
        model.addAttribute(HAS_NEXT, true);
        model.addAttribute(HAS_PREVIOUS, false);

        return "quiz";
    }

    @PostMapping(value = "/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex, Model model) {
        model.addAttribute(QUERY, quizService.getNextQuery(currentIndex));
        setActionAttributes(currentIndex + 1, model);
        return "quiz";
    }

    @PostMapping(value = "/quiz/prevQuery")
    public String prevQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex, Model model) {
        model.addAttribute(QUERY, quizService.getPrevQuery(currentIndex));
        setActionAttributes(currentIndex - 1, model);
        return "quiz";
    }

    private void setActionAttributes(int index, Model model) {
        model.addAttribute(CURRENT_INDEX, index);
        model.addAttribute(HAS_NEXT, quizService.hasNext(index));
        model.addAttribute(HAS_PREVIOUS, quizService.hasPrevious(index));
    }
}
