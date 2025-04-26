package com.adaptive.quiz.controller;

import com.adaptive.quiz.model.ActualQuiz;
import com.adaptive.quiz.model.Choice;
import com.adaptive.quiz.model.Question;
import com.adaptive.quiz.model.Result;
import com.adaptive.quiz.model.UserQuiz;
import com.adaptive.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class QuizController {
    public static final String SESSION_REGISTER = "register";
    public static final String DISABLE_PREVIOUS_ACTION = "disablePreviousAction";
    public static final String DISABLE_NEXT_ACTION = "disableNextAction";
    private final QuizService quizService;

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
                .choices(getUIChoices(question.getChoices(), actualQuiz.getAnswer()))
                .uiIndex(0)
                .totalQuestions(totalQuestions)
                .build();

        model.addAttribute("previousIndex", -1);
        model.addAttribute("nextIndex", 1);
        model.addAttribute("question", uiQuestion);

        model.addAttribute(DISABLE_PREVIOUS_ACTION, true);
        model.addAttribute(DISABLE_NEXT_ACTION, false);

        return "quiz";
    }

    @RequestMapping(value = "/v1/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = "actualQuizId") int actualQuizId,
                            @RequestParam(name = "quizId") int quizId,
                            @RequestParam(name = "questionId") int questionId,
                            @RequestParam(name = "previousIndex") int previousIndex,
                            @RequestParam(name = "nextIndex") int nextIndex,
                            @RequestParam(name = "quizItem", defaultValue = "-1") int choice,
                            Model model) {

        quizService.updateActualQuiz(actualQuizId, choice);
        UserQuiz userQuiz = quizService.findUserQuiz(quizId);
        ActualQuiz actualQuiz = quizService.findActualQuizByUserQuizIdAndUiIndex(userQuiz.getId(), nextIndex);
        Question question;
        if (actualQuiz == null) {
            question = quizService.findNextQuestion(quizId, questionId, actualQuizId, nextIndex);
            actualQuiz = quizService.registerIntoActualQuiz(userQuiz, question, nextIndex);
        } else {
            question = quizService.findQuestion(actualQuiz.getQuestionId());
        }
        int totalQuestions = quizService.getTotalQuestions(quizId) - 1;

        model.addAttribute("previousIndex", previousIndex + 1);
        model.addAttribute(DISABLE_PREVIOUS_ACTION, false);
        nextIndex = nextIndex + 1;
        model.addAttribute("nextIndex", nextIndex);
        model.addAttribute(DISABLE_NEXT_ACTION, nextIndex == totalQuestions);

        model.addAttribute("question", getUiQuestion(actualQuiz, userQuiz, question, totalQuestions));

        return "quiz";
    }

    @RequestMapping(value = "/v1/quiz/prevQuery")
    public String prevQuery(@RequestParam(name = "actualQuizId") int actualQuizId,
                            @RequestParam(name = "quizId") int quizId,
                            @RequestParam(name = "previousIndex") int previousIndex,
                            @RequestParam(name = "nextIndex") int nextIndex,
                            @RequestParam(name = "quizItem", defaultValue = "-1") int choice,
                            Model model) {

        quizService.updateActualQuiz(actualQuizId, choice);
        UserQuiz userQuiz = quizService.findUserQuiz(quizId);
        ActualQuiz actualQuiz = quizService.findActualQuizByUserQuizIdAndUiIndex(userQuiz.getId(), previousIndex);
        Question question = quizService.findQuestion(actualQuiz.getQuestionId());
        int totalQuestions = quizService.getTotalQuestions(quizId) - 1;

        previousIndex = previousIndex - 1;
        model.addAttribute("previousIndex", previousIndex);
        model.addAttribute(DISABLE_PREVIOUS_ACTION, previousIndex < 0);
        model.addAttribute("nextIndex", nextIndex - 1);
        model.addAttribute(DISABLE_NEXT_ACTION, false);
        model.addAttribute("question", getUiQuestion(actualQuiz, userQuiz, question, totalQuestions));

        return "quiz";
    }

    @RequestMapping(value = "/v1/quiz/finishQuiz")
    public String finishQuiz(@RequestParam(name = "actualQuizId") int actualQuizId,
                             @RequestParam(name = "quizItem", defaultValue = "-1") int choice,
                             Model model) {

        quizService.updateActualQuiz(actualQuizId, choice);
        Result result = quizService.finishQuiz(actualQuizId);

        model.addAttribute("result", result);
        return "result";
    }


    private UIQuestion getUiQuestion(ActualQuiz actualQuiz, UserQuiz userQuiz, Question question, int totalQuestions) {
        return UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(userQuiz.getQuizId())
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices(), actualQuiz.getAnswer()))
                .uiIndex(actualQuiz.getUiIndex())
                .totalQuestions(totalQuestions)
                .build();
    }

    static List<UIChoice> getUIChoices(Set<Choice> choices, int answer) {
        return choices.stream()
                .map(c -> UIChoice.builder()
                        .data(c.getData())
                        .id(c.getId())
                        .selected(c.getId() == answer)
                        .build())
                .toList();
    }


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
