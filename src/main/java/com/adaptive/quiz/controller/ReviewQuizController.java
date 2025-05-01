package com.adaptive.quiz.controller;

import com.adaptive.quiz.model.ActualQuiz;
import com.adaptive.quiz.model.Choice;
import com.adaptive.quiz.model.Question;
import com.adaptive.quiz.model.UserQuiz;
import com.adaptive.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

import static com.adaptive.quiz.controller.QuizController.getUIChoices;

/**
 * Controller class for review quiz answers. This provides read only actions for the quiz.
 */
@Controller
@RequiredArgsConstructor
public class ReviewQuizController {
    private final QuizService quizService;

    /**
     * Controller method for start reviewing quiz answers.
     * In UI (browser page) green banner with text "Correct Answer" for right answer and
     * red banner with text "Incorrect answer" for wrong answer will be displayed.
     *
     * @param quizId
     * @param questionIndex
     * @param model
     * @return
     */
    @RequestMapping(value = "/v1/review/quiz/start")
    public String start(@RequestParam(name = "quizId") int quizId,
                        @RequestParam(name = "qIndex") int questionIndex,
                        Model model) {
        UserQuiz userQuiz = quizService.findUserQuiz(quizId);
        ActualQuiz actualQuiz = quizService.findActualQuizByUserQuizIdAndUiIndex(userQuiz.getId(), questionIndex);

        model.addAttribute("reviewDone", actualQuiz == null);
        if (actualQuiz == null) {
            return "quiz_review";
        }

        Question question = quizService.findQuestion(actualQuiz.getQuestionId());
        Integer rightAnswer = question.getChoices().stream().filter(Choice::isAnswer).findFirst().map(Choice::getId).orElse(-1);
        Integer providedAnswer = actualQuiz.getAnswer();
        UIQuestion uiQuestion = UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(userQuiz.getQuizId())
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices(), actualQuiz.getAnswer()))
                .uiIndex(questionIndex)
                .build();

        model.addAttribute("question", uiQuestion);
        model.addAttribute("previousIndex", -1);
        model.addAttribute("nextIndex", 1);
        model.addAttribute("quizId", quizId);
        model.addAttribute("answerStatus", Objects.equals(rightAnswer, providedAnswer)
                ? 1 : 0);

        return "quiz_review";
    }

    /**
     * Controller method for action next to reviewing quiz answers.
     * In UI (browser page) green banner with text "Correct Answer" for right answer and
     * red banner with text "Incorrect answer" for wrong answer will be displayed.
     *
     * @param quizId
     * @param previousIndex
     * @param nextIndex
     * @param model
     * @return
     */
    @RequestMapping(value = "/v1/review/quiz/nextQuery")
    public String nextQuery(@RequestParam(name = "quizId") int quizId,
                            @RequestParam(name = "previousIndex") int previousIndex,
                            @RequestParam(name = "nextIndex") int nextIndex,
                            Model model) {

        UserQuiz userQuiz = quizService.findUserQuiz(quizId);
        ActualQuiz actualQuiz = quizService.findActualQuizByUserQuizIdAndUiIndex(userQuiz.getId(), nextIndex);

        model.addAttribute("reviewDone", actualQuiz == null);
        if (actualQuiz == null) {
            return "quiz_review";
        }

        Question question = quizService.findQuestion(actualQuiz.getQuestionId());
        Integer rightAnswer = question.getChoices().stream().filter(Choice::isAnswer).findFirst().map(Choice::getId).orElse(-1);
        Integer providedAnswer = actualQuiz.getAnswer();
        int totalQuestions = quizService.getTotalQuestions(quizId) - 1;

        UIQuestion uiQuestion = UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(userQuiz.getQuizId())
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices(), actualQuiz.getAnswer()))
                .uiIndex(nextIndex)
                .build();

        model.addAttribute("question", uiQuestion);
        model.addAttribute("previousIndex", previousIndex + 1);
        model.addAttribute("nextIndex", nextIndex < totalQuestions ? nextIndex + 1 : -1);
        model.addAttribute("quizId", quizId);
        model.addAttribute("answerStatus", Objects.equals(rightAnswer, providedAnswer)
                ? 1 : 0);
        return "quiz_review";
    }

    /**
     * Controller method for action previous to reviewing quiz answers.
     * In UI (browser page) green banner with text "Correct Answer" for right answer and
     * red banner with text "Incorrect answer" for wrong answer will be displayed.
     *
     * @param quizId
     * @param previousIndex
     * @param nextIndex
     * @param model
     * @return
     */
    @RequestMapping(value = "/v1/review/quiz/prevQuery")
    public String prevQuery(@RequestParam(name = "quizId") int quizId,
                            @RequestParam(name = "previousIndex") int previousIndex,
                            @RequestParam(name = "nextIndex") int nextIndex,
                            Model model) {

        UserQuiz userQuiz = quizService.findUserQuiz(quizId);
        ActualQuiz actualQuiz = quizService.findActualQuizByUserQuizIdAndUiIndex(userQuiz.getId(), previousIndex);

        Question question = quizService.findQuestion(actualQuiz.getQuestionId());
        Integer rightAnswer = question.getChoices().stream().filter(Choice::isAnswer).findFirst().map(Choice::getId).orElse(-1);
        Integer providedAnswer = actualQuiz.getAnswer();

        UIQuestion uiQuestion = UIQuestion.builder()
                .actualQuizId(actualQuiz.getId())
                .quizId(userQuiz.getQuizId())
                .questionId(question.getId())
                .question(question.getQuestion())
                .subject(question.getSubject())
                .topic(question.getTopic())
                .choices(getUIChoices(question.getChoices(), actualQuiz.getAnswer()))
                .uiIndex(nextIndex)
                .build();

        model.addAttribute("question", uiQuestion);
        model.addAttribute("previousIndex", previousIndex - 1);
        model.addAttribute("nextIndex", nextIndex > 0 ? nextIndex - 1 : 1);
        model.addAttribute("quizId", quizId);
        model.addAttribute("answerStatus", Objects.equals(rightAnswer, providedAnswer)
                ? 1 : 0);
        return "quiz_review";
    }

}
