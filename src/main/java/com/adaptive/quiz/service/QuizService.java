package com.adaptive.quiz.service;

import com.adaptive.quiz.model.ActualQuiz;
import com.adaptive.quiz.model.Choice;
import com.adaptive.quiz.model.Question;
import com.adaptive.quiz.model.Quiz;
import com.adaptive.quiz.model.Result;
import com.adaptive.quiz.model.UserQuiz;
import com.adaptive.quiz.repository.ActualQuizRepository;
import com.adaptive.quiz.repository.QuestionRepository;
import com.adaptive.quiz.repository.QuizRepository;
import com.adaptive.quiz.repository.ResultRepository;
import com.adaptive.quiz.repository.UserQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final UserQuizRepository userQuizRepository;
    private final QuizRepository quizRepository;
    private final ActualQuizRepository actualQuizRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;


    public UserQuiz start(int quizId) {
        String username = getUsername();
        List<UserQuiz> existingUserQuiz = userQuizRepository.findUserQuizByUsername(username);
        if (!CollectionUtils.isEmpty(existingUserQuiz)) {
            userQuizRepository.deleteAll(existingUserQuiz);
            actualQuizRepository.deleteAllByUserQuizId(existingUserQuiz.stream().map(UserQuiz::getId).toList());
        }

        UserQuiz userQuiz = new UserQuiz();
        userQuiz.setQuizId(quizId);
        userQuiz.setUsername(username);
        userQuiz.setStatus(1);
        return userQuizRepository.save(userQuiz);
    }

    private static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
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
        return userQuizRepository.findByIdAndUsername(quizId, getUsername());
    }

    public int getTotalQuestions(int quizId) {
        return quizRepository.findById(quizId).map(q -> q.getQuestions().size()).orElse(0);
    }

    public ActualQuiz findActualQuiz(int actualQuizId) {
        return actualQuizRepository.findById(actualQuizId).orElse(null);
    }

    public ActualQuiz findActualQuizByUserQuizIdAndUiIndex(int userQuizId, int uiIndex) {
        return actualQuizRepository.findActualQuizByUserQuizIdAndUiIndex(userQuizId, uiIndex);
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

    public Result finishQuiz(int actualQuizId) {

        ActualQuiz actualQuiz = actualQuizRepository.findById(actualQuizId).orElse(null);
        Result result = new Result();
        if (actualQuiz != null) {
            List<ActualQuiz> actualAllAnswer = actualQuizRepository.findAllByUserQuizId(actualQuiz.getUserQuizId());
            List<ActualQuiz> allAnswer = actualAllAnswer.stream().filter(a -> a.getAnswer() != -1).toList();

            UserQuiz userQuiz = userQuizRepository.findById(actualQuiz.getUserQuizId()).orElse(null);
            assert userQuiz != null;
            result.setUserQuizId(userQuiz.getId());

            Quiz quiz = quizRepository.findById(userQuiz.getQuizId()).orElse(null);
            assert quiz != null;
            result.setQuizId(quiz.getId());
            result.setQuizName(quiz.getName());
            result.setUsername(userQuiz.getUsername());

            int totalQuestions = quiz.getQuestions().size();
            int answeredQuestions = allAnswer.size();
            int unAnsweredQuestions = totalQuestions - answeredQuestions;

            result.setAnsweredQuestions(answeredQuestions);
            result.setUnAnsweredQuestions(unAnsweredQuestions);
            result.setTotalQuestions(totalQuestions);

            Map<Integer, Integer> level1Questions = getQuestionForLevel(quiz, 1);
            List<ActualQuiz> level1RightAnswers = getRightAnswersForLevel(allAnswer, level1Questions);
            result.setLevel1(level1RightAnswers.size());

            Map<Integer, Integer> level2Questions = getQuestionForLevel(quiz, 2);
            List<ActualQuiz> level2RightAnswers = getRightAnswersForLevel(allAnswer, level2Questions);
            result.setLevel2(level2RightAnswers.size());

            Map<Integer, Integer> level3Questions = getQuestionForLevel(quiz, 3);
            List<ActualQuiz> level3RightAnswers = getRightAnswersForLevel(allAnswer, level3Questions);
            result.setLevel3(level3RightAnswers.size());

            Map<Integer, Integer> level4Questions = getQuestionForLevel(quiz, 4);
            List<ActualQuiz> level4RightAnswers = getRightAnswersForLevel(allAnswer, level4Questions);
            result.setLevel4(level4RightAnswers.size());

            Map<Integer, Integer> level5Questions = getQuestionForLevel(quiz, 5);
            List<ActualQuiz> level5RightAnswers = getRightAnswersForLevel(allAnswer, level5Questions);
            result.setLevel5(level5RightAnswers.size());


            int rightAnswers = level1RightAnswers.size()
                    + level2RightAnswers.size()
                    + level3RightAnswers.size()
                    + level4RightAnswers.size()
                    + level5RightAnswers.size();
            result.setRightAnswers(rightAnswers);
            result.setWrongAnswers(totalQuestions - rightAnswers);
            double percentage =
                    ((double)
                            rightAnswers
                            / (double) totalQuestions) * 100;

            result.setPercentageCorrectAnswers(percentage);
            resultRepository.save(result);
        }
        return result;
    }

    private static List<ActualQuiz> getRightAnswersForLevel(List<ActualQuiz> allAnswer, Map<Integer, Integer> level1Questions) {
        return allAnswer.stream()
                .filter(a -> level1Questions.containsKey(a.getQuestionId()))
                .filter(a -> level1Questions.get(a.getQuestionId()) == a.getAnswer())
                .toList();
    }

    private static Map<Integer, Integer> getQuestionForLevel(Quiz quiz, int difficulty) {
        return quiz.getQuestions()
                .stream()
                .filter(q -> q.getDifficulty() == difficulty)
                .collect(Collectors.toMap(Question::getId, q -> q.getChoices().stream()
                        .filter(Choice::isAnswer)
                        .map(Choice::getId)
                        .findFirst()
                        .orElse(-1)
                ));
    }

    public List<Result> findLastFiveResults() {
        return resultRepository.findAllByUsername(getUsername());
    }
}
