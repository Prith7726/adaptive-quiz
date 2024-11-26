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
    public static final String SHOW_FINISH = "showFinish";
    private final QuizService quizService;
    private final RegisterService registerService;

    @PostMapping(value = "/quiz/start")
    public String start(@RequestParam(name = "name") String name, Model model, HttpServletRequest request) {
        RegisterResponse response = registerService.register(name);
        request.getSession(true).setAttribute(SESSION_REGISTER, response);

        model.addAttribute(QUERY, quizService.getQuery(getUser(request)));
        model.addAttribute(CURRENT_INDEX, 0);
        model.addAttribute(HAS_NEXT, true);
        model.addAttribute(HAS_PREVIOUS, false);
        model.addAttribute(SHOW_FINISH, false);

        return "quiz";
    }

    @PostMapping(value = "/quiz/restart")
    public String restart(Model model, HttpServletRequest request) {
        RegisterResponse registerResponse = (RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER);
        registerService.deRegister(registerResponse.name());
        request.getSession().invalidate();
        RegisterResponse response = registerService.register(registerResponse.name());
        request.getSession(true).setAttribute(SESSION_REGISTER, response);

        model.addAttribute(QUERY, quizService.getQuery(getUser(request)));
        model.addAttribute(CURRENT_INDEX, 0);
        model.addAttribute(HAS_NEXT, true);
        model.addAttribute(HAS_PREVIOUS, false);
        model.addAttribute(SHOW_FINISH, false);

        return "quiz";
    }

    @PostMapping(value = "/quiz/checkAnswer")
    public String checkAnswer(
            @RequestParam(name = CURRENT_INDEX) int currentIndex,
            @RequestParam(name = "quizItem", required = false) String answer,
            HttpServletRequest request,
            Model model
    ) {
        String user = getUser(request);
        model.addAttribute(
                "answer_status",
                quizService.checkAnswer(user, currentIndex, answer) ? "Right Answer!" : "Please try again!");
        model.addAttribute(QUERY, quizService.getQuery(user, currentIndex));
        setActionAttributes(user, currentIndex, model);
        return "quiz";
    }

    String getUser(HttpServletRequest request) {
        return ((RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER)).name();
    }

    @PostMapping(value = "/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex,
                            @RequestParam(name = "quizItem", required = false) String answer,
                            HttpServletRequest request,
                            Model model) {
        String user = getUser(request);
        model.addAttribute(QUERY, quizService.getNextQuery(user, currentIndex, answer));
        setActionAttributes(user, currentIndex + 1, model);
        return "quiz";
    }

    @PostMapping(value = "/quiz/prevQuery")
    public String prevQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex,
                            @RequestParam(name = "quizItem", required = false) String answer,
                            HttpServletRequest request,
                            Model model) {
        String user = getUser(request);
        model.addAttribute(QUERY, quizService.getPrevQuery(user, currentIndex, answer));
        setActionAttributes(user, currentIndex - 1, model);
        return "quiz";
    }

    @PostMapping(value = "/quiz/finishQuiz")
    public String finishQuiz(@RequestParam(name = CURRENT_INDEX) int currentIndex,
                             @RequestParam(name = "quizItem", required = false) String answer,
                             HttpServletRequest request,
                             Model model) {
        String user = getUser(request);
        model.addAttribute("report", quizService.finishQuiz(user, currentIndex, answer));
        return "report";
    }

    @PostMapping(value = "/quiz/logout")
    public String logout(HttpServletRequest request) {
        registerService.deRegister(getUser(request));
        request.getSession().invalidate();
        return "index";
    }

    private void setActionAttributes(String user, int index, Model model) {
        model.addAttribute(CURRENT_INDEX, index);
        model.addAttribute(HAS_NEXT, quizService.hasNext(user, index));
        model.addAttribute(HAS_PREVIOUS, quizService.hasPrevious(index));
        model.addAttribute(SHOW_FINISH, quizService.hasAllAnswered(user));
    }
}
