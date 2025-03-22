package com.adaptive.quiz.controller;

import com.adaptive.quiz.model.ActualQuiz;
import com.adaptive.quiz.model.Choice;
import com.adaptive.quiz.model.Question;
import com.adaptive.quiz.model.UserQuiz;
import com.adaptive.quiz.repository.ActualQuizRepository;
import com.adaptive.quiz.repository.QuestionRepository;
import com.adaptive.quiz.service.QuizService;
import com.adaptive.quiz.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

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
    private final ActualQuizRepository actualQuizRepository;
    private final QuestionRepository questionRepository;


    @RequestMapping(value = "/v1/quiz/start")
    public String start(@RequestParam(name = "id") int id, Model model) {

        UserQuiz userQuiz = quizService.start(id);
        Question question = quizService.getFirstQuery(id);
        int totalQuestions = quizService.getTotalQuestions(id);
        ActualQuiz actualQuiz = quizService.registerIntoActualQuiz(userQuiz, question, 0);
        model.addAttribute("actualQuiz", actualQuiz);


        UIQuestion uiQuestion = UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(userQuiz.getQuizId())
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices()))
                .uiIndex(0)
                .totalQuestions(totalQuestions)
                .build();

        model.addAttribute("previousIndex", -1);
        model.addAttribute("nextIndex", 1);
        model.addAttribute("question", uiQuestion);


        return "quiz";
    }

    @RequestMapping(value = "/v1/quiz/updateChoice")
    public String updateChoice(@RequestParam(name = "actualQuizId") int actualQuizId,
                               @RequestParam(name = "quizId") int quizId,
                               @RequestParam(name = "questionId") int questionId,
                               @RequestParam(name = "currentIndex") int currentIndex,
                               @RequestParam(name = "previousIndex") int previousIndex,
                               @RequestParam(name = "nextIndex") int nextIndex,
                               @RequestParam(name = "quizItem") int choice,
                               Model model) {

        ActualQuiz actualQuiz = quizService.updateActualQuiz(actualQuizId, choice);
        Question question = quizService.findQuestion(questionId);
        int totalQuestions = quizService.getTotalQuestions(quizId);

        UIQuestion uiQuestion = UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(quizId)
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices(), choice))
                .totalQuestions(totalQuestions)
                .uiIndex(currentIndex)
                .build();

        model.addAttribute("previousIndex", previousIndex);
        model.addAttribute("nextIndex", nextIndex);
        model.addAttribute("question", uiQuestion);
        return "quiz";
    }

    @RequestMapping(value = "/v1/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = "actualQuizId") int actualQuizId,
                            @RequestParam(name = "quizId") int quizId,
                            @RequestParam(name = "questionId") int questionId,
                            @RequestParam(name = "currentIndex") int currentIndex,
                            @RequestParam(name = "previousIndex") int previousIndex,
                            @RequestParam(name = "nextIndex") int nextIndex,
                            Model model) {

        Question question = quizService.findNextQuestion(quizId, questionId, actualQuizId, nextIndex);
        UserQuiz userQuiz = quizService.findUserQuiz(quizId);
        ActualQuiz actualQuiz = quizService.registerIntoActualQuiz(userQuiz, question, nextIndex);
        int totalQuestions = quizService.getTotalQuestions(quizId);

        UIQuestion uiQuestion = UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(userQuiz.getQuizId())
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices()))
                .uiIndex(actualQuiz.getUiIndex())
                .totalQuestions(totalQuestions)
                .build();

        model.addAttribute("previousIndex", previousIndex + 1);
        model.addAttribute("nextIndex", nextIndex < totalQuestions ? nextIndex : -1);
        model.addAttribute("question", uiQuestion);

        return "quiz";
    }

    private List<UIChoice> getUIChoices(Set<Choice> choices, int selectedChoice) {
        return choices.stream()
                .map(c -> UIChoice.builder().data(c.getData()).id(c.getId())
                        .selected(c.getId() == selectedChoice)
                        .build())
                .sorted(Comparator.comparingInt(UIChoice::getId))
                .toList();
    }

    private List<UIChoice> getUIChoices(Set<Choice> choices) {
        return choices.stream()
                .map(c -> UIChoice.builder().data(c.getData()).id(c.getId()).build())
                .toList();
    }

//    @PostMapping(value = "/quiz/restart")
//    public String restart(Model model, HttpServletRequest request) {
//        RegisterResponse registerResponse = (RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER);
//        registerService.deRegister(registerResponse.uuid());
//        request.getSession().invalidate();
//        RegisterResponse response = registerService.register(registerResponse.name());
//        return setDefaults(model, request, response);
//    }
//
//    @PostMapping(value = "/quiz/reviewAnswers")
//    public String reviewAnswers(Model model, HttpServletRequest request) {
//        setDefaults(model, request, (RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER));
//        return "review";
//    }
//
//    private String setDefaults(Model model, HttpServletRequest request, RegisterResponse response) {
//        request.getSession(true).setAttribute(SESSION_REGISTER, response);
//
//        model.addAttribute(QUERY, quizService.getQuery(getUser(request)));
//        model.addAttribute(CURRENT_INDEX, 0);
//        model.addAttribute(HAS_NEXT, true);
//        model.addAttribute(HAS_PREVIOUS, false);
//        model.addAttribute(SHOW_FINISH, false);
//
//        return "quiz";
//    }
//
//    @PostMapping(value = "/quiz/checkAnswer")
//    public String checkAnswer(
//            @RequestParam(name = CURRENT_INDEX) int currentIndex,
//            @RequestParam(name = "quizItem", required = false) String answer,
//            HttpServletRequest request,
//            Model model
//    ) {
//        UUID uuid = getUser(request);
//        model.addAttribute(
//                "answer_status",
//                quizService.checkAnswer(uuid, currentIndex, answer) ? "Right Answer!" : "Please try again!");
//        model.addAttribute(QUERY, quizService.getQuery(uuid, currentIndex));
//        setActionAttributes(uuid, currentIndex, model);
//        return "quiz1";
//    }
//
//    UUID getUser(HttpServletRequest request) {
//        return ((RegisterResponse) request.getSession().getAttribute(SESSION_REGISTER)).uuid();
//    }
//
//    @PostMapping(value = "/quiz/nextQuery")
//    public String nextQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex,
//                            @RequestParam(name = "quizItem", required = false) String answer,
//                            @RequestParam(name = "target", required = false) String target,
//                            HttpServletRequest request,
//                            Model model) {
//        UUID uuid = getUser(request);
//        model.addAttribute(QUERY, quizService.getNextQuery(uuid, currentIndex, answer));
//        setActionAttributes(uuid, currentIndex + 1, model);
//        return StringUtils.hasText(target) ? target : "quiz1";
//    }
//
//    @PostMapping(value = "/quiz/prevQuery")
//    public String prevQuery(@RequestParam(name = CURRENT_INDEX) int currentIndex,
//                            @RequestParam(name = "quizItem", required = false) String answer,
//                            @RequestParam(name = "target", required = false) String target,
//                            HttpServletRequest request,
//                            Model model) {
//        UUID uuid = getUser(request);
//        model.addAttribute(QUERY, quizService.getPrevQuery(uuid, currentIndex, answer));
//        setActionAttributes(uuid, currentIndex - 1, model);
//        return StringUtils.hasText(target) ? target : "quiz1";
//    }
//
//    @PostMapping(value = "/quiz/finishQuiz")
//    public String finishQuiz(@RequestParam(name = CURRENT_INDEX) int currentIndex,
//                             @RequestParam(name = "quizItem", required = false) String answer,
//                             HttpServletRequest request,
//                             Model model) {
//        UUID uuid = getUser(request);
//        model.addAttribute("report", quizService.finishQuiz(uuid, currentIndex, answer));
//        return "report";
//    }
//
//    @PostMapping(value = "/quiz/logout")
//    public String logout(HttpServletRequest request) {
//        registerService.deRegister(getUser(request));
//        request.getSession().invalidate();
//        return "index";
//    }
//
//    private void setActionAttributes(UUID uuid, int index, Model model) {
//        model.addAttribute(CURRENT_INDEX, index);
//        model.addAttribute(HAS_NEXT, quizService.hasNext(uuid, index));
//        model.addAttribute(HAS_PREVIOUS, quizService.hasPrevious(index));
//        model.addAttribute(SHOW_FINISH, quizService.hasAllAnswered(uuid));
//    }
}
