package com.adaptive.quiz.controller;

import com.adaptive.quiz.service.QuizService;
import com.adaptive.quiz.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

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
        return setDefaults(model, request, response);
    }

    @PostMapping(value = "/quiz/restart")
    public String restart(Model model, HttpServletRequest request) {
        RegisterResponse registerResponse = (RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER);
        registerService.deRegister(registerResponse.uuid());
        request.getSession().invalidate();
        RegisterResponse response = registerService.register(registerResponse.name());
        return setDefaults(model, request, response);
    }

    @PostMapping(value = "/quiz/reviewAnswers")
    public String reviewAnswers(Model model, HttpServletRequest request) {
        setDefaults(model, request, (RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER));
        return "review";
    }

    private String setDefaults(Model model, HttpServletRequest request, RegisterResponse response) {
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
        UUID uuid = getUser(request);
        model.addAttribute(
                "answer_status",
                quizService.checkAnswer(uuid, currentIndex, answer) ? "Right Answer!" : "Please try again!");
        model.addAttribute(QUERY, quizService.getQuery(uuid, currentIndex));
        setActionAttributes(uuid, currentIndex, model);
        return "quiz";
    }

    UUID getUser(HttpServletRequest request) {
        return ((RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER)).uuid();
    }

    @PostMapping(value = "/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex,
                            @RequestParam(name = "quizItem", required = false) String answer,
                            @RequestParam(name = "target", required = false) String target,
                            HttpServletRequest request,
                            Model model) {
        UUID uuid = getUser(request);
        model.addAttribute(QUERY, quizService.getNextQuery(uuid, currentIndex, answer));
        setActionAttributes(uuid, currentIndex + 1, model);
        return StringUtils.hasText(target) ? target : "quiz";
    }

    @PostMapping(value = "/quiz/prevQuery")
    public String prevQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex,
                            @RequestParam(name = "quizItem", required = false) String answer,
                            @RequestParam(name = "target", required = false) String target,
                            HttpServletRequest request,
                            Model model) {
        UUID uuid = getUser(request);
        model.addAttribute(QUERY, quizService.getPrevQuery(uuid, currentIndex, answer));
        setActionAttributes(uuid, currentIndex - 1, model);
        return StringUtils.hasText(target) ? target : "quiz";
    }

    @PostMapping(value = "/quiz/finishQuiz")
    public String finishQuiz(@RequestParam(name = CURRENT_INDEX) int currentIndex,
                             @RequestParam(name = "quizItem", required = false) String answer,
                             HttpServletRequest request,
                             Model model) {
        UUID uuid = getUser(request);
        model.addAttribute("report", quizService.finishQuiz(uuid, currentIndex, answer));
        return "report";
    }

    @PostMapping(value = "/quiz/logout")
    public String logout(HttpServletRequest request) {
        registerService.deRegister(getUser(request));
        request.getSession().invalidate();
        return "index";
    }

    private void setActionAttributes(UUID uuid, int index, Model model) {
        model.addAttribute(CURRENT_INDEX, index);
        model.addAttribute(HAS_NEXT, quizService.hasNext(uuid, index));
        model.addAttribute(HAS_PREVIOUS, quizService.hasPrevious(index));
        model.addAttribute(SHOW_FINISH, quizService.hasAllAnswered(uuid));
    }
}
