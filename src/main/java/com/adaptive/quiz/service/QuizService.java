package com.adaptive.quiz.service;

import com.adaptive.quiz.model.ActualQuiz;
import com.adaptive.quiz.model.Choice;
import com.adaptive.quiz.model.Question;
import com.adaptive.quiz.model.Quiz;
import com.adaptive.quiz.model.UserQuiz;
import com.adaptive.quiz.repository.ActualQuizRepository;
import com.adaptive.quiz.repository.QuestionRepository;
import com.adaptive.quiz.repository.QuizRepository;
import com.adaptive.quiz.repository.UserQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final UserQuizRepository userQuizRepository;
    private final QuizRepository quizRepository;
    private final ActualQuizRepository actualQuizRepository;
    private final QuestionRepository questionRepository;


    public UserQuiz start(int quizId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserQuiz existingUserQuiz = userQuizRepository.findByIdAndUsername(quizId, username);
        if (existingUserQuiz != null) {
            return existingUserQuiz;
        }

        UserQuiz userQuiz = new UserQuiz();
        userQuiz.setQuizId(quizId);
        userQuiz.setUsername(username);
        userQuiz.setStatus(1);
        return userQuizRepository.save(userQuiz);
    }


    public Question getFirstQuery(int quizId) {
        return quizRepository.findById(quizId)
                .flatMap(quiz -> quiz.getQuestions()
                        .stream()
                        .filter(q -> 1 == q.getDifficulty())
                        .findFirst()).orElse(null);
    }

    public ActualQuiz registerIntoActualQuiz(UserQuiz userQuiz, Question question, int uiIndex) {
        ActualQuiz actualQuiz = new ActualQuiz();
        actualQuiz.setUserQuizId(userQuiz.getId());
        actualQuiz.setQuestionId(question.getId());
        actualQuiz.setUiIndex(uiIndex);
        return actualQuizRepository.save(actualQuiz);
    }

    public UserQuiz findUserQuiz(int quizId) {
        return userQuizRepository.findById(quizId).orElse(null);
    }

    public int getTotalQuestions(int quizId) {
        return quizRepository.findById(quizId).map(q -> q.getQuestions().size()).orElse(0);
    }

    public ActualQuiz findActualQuiz(int actualQuizId) {
        return actualQuizRepository.findById(actualQuizId).orElse(null);
    }

    public ActualQuiz updateActualQuiz(int actualQuizId, int answer) {
        ActualQuiz entity = actualQuizRepository.findById(actualQuizId).orElse(null);
        assert entity != null;
        entity.setAnswer(answer);
        return actualQuizRepository.save(entity);
    }

    public Question findQuestion(int questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    public Question findNextQuestion(int quizId, int questionId, int actualQuizId, int index) {
        // validate current answer
        Question question = questionRepository.findById(questionId).orElse(null);
        assert question != null;
        ActualQuiz actualQuiz = findActualQuiz(actualQuizId);

        int expectedAnswer = question.getChoices().stream().filter(Choice::isAnswer).findFirst().map(Choice::getId).orElse(-1);
        final int level = question.getDifficulty();

        final boolean increment = expectedAnswer == actualQuiz.getAnswer();

        return quizRepository.findById(quizId)
                .map(Quiz::getQuestions)
                .map(qs -> getNextQuestion(qs, level, actualQuiz, increment))
                .orElse(null);
    }

    private Question getNextQuestion(Set<Question> questions, int level, ActualQuiz actualQuiz, boolean actualIncrement) {
        List<Integer> questionsIdByUserQuizId = actualQuizRepository.findQuestionsIdByUserQuizId(actualQuiz.getUserQuizId());

        List<Question> unUsedQuestions = questions.stream()
                .filter(q -> !isQuestionUsed(q.getId(), questionsIdByUserQuizId))
                .toList();

        return getQuestion(unUsedQuestions, level, actualIncrement);

    }

    private Question getQuestion(List<Question> unUsedQuestions, int level, boolean actualIncrement) {
        level = actualIncrement ? level + 1 : level - 1;
        level = level <= 0 ? 1 : level;
        level = Math.min(level, 5);

        Optional<Question> levelOneQuestions = unUsedQuestions.stream()
                .filter(q -> q.getDifficulty() == 1)
                .findFirst();

        if (level == 1 && levelOneQuestions.isPresent()) {
            return levelOneQuestions.get();
        }

        Optional<Question> levelTwoQuestions = unUsedQuestions.stream()
                .filter(q -> q.getDifficulty() == 2)
                .findFirst();

        if ((level == 1 || level == 2) && (levelTwoQuestions.isPresent())) {
            return levelTwoQuestions.get();
        }

        Optional<Question> levelThreeQuestions = unUsedQuestions.stream()
                .filter(q -> q.getDifficulty() == 3)
                .findFirst();


        if (level <= 3 && levelThreeQuestions.isPresent()) {
            return levelThreeQuestions.get();
        }

        Optional<Question> levelFourQuestions = unUsedQuestions.stream()
                .filter(q -> q.getDifficulty() == 4)
                .findFirst();
        if (level <= 4 && levelFourQuestions.isPresent()) {
            return levelFourQuestions.get();
        }

        Optional<Question> levelFiveQuestions = unUsedQuestions.stream()
                .filter(q -> q.getDifficulty() == 5)
                .findFirst();
        return levelFiveQuestions
                .orElseGet(() -> levelFourQuestions
                        .orElseGet(() -> levelThreeQuestions
                                .orElseGet(() -> levelTwoQuestions
                                        .orElseGet(() -> levelOneQuestions
                                                .orElse(null)))));


    }

    private boolean isQuestionUsed(int questionId, List<Integer> existingUsedQuestions) {
        return existingUsedQuestions.stream()
                .anyMatch(q -> questionId == q);

    }
}
