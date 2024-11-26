package com.adaptive.quiz.service;

import com.adaptive.quiz.controller.Choice;
import com.adaptive.quiz.controller.Query;
import com.adaptive.quiz.controller.QuizReport;
import com.adaptive.quiz.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final DataRepository dataRepository;

    public void quiz() {
        log.warn("Am I initialized ?");
    }

    public Query getQuery(String user) {
        return getQuery(user, 0);
    }

    public Query getQuery(String user, int index) {
        return getAllQueries(user)[index];
    }

    public Query getNextQuery(String user, int currentIndex, String answer) {
        setSelected(user, currentIndex, answer);
        return getQuery(user, currentIndex + 1);
    }

    public Query getPrevQuery(String user, int currentIndex, String answer) {
        setSelected(user, currentIndex, answer);
        return getQuery(user, currentIndex - 1);
    }

    private void setSelected(String user, int currentIndex, String answer) {
        if (StringUtils.hasText(answer)) {
            for (Choice c : getQuery(user, currentIndex).getChoices()) {
                c.setSelected(c.getData().equalsIgnoreCase(answer));
            }
        }
    }

    public boolean hasNext(String user, int currentIndex) {
        return currentIndex + 1 < getAllQueries(user).length;
    }

    public boolean hasPrevious(int currentIndex) {
        return currentIndex - 1 >= 0;
    }

    public boolean checkAnswer(String user, int currentIndex, String answer) {
        setSelected(user, currentIndex, answer);
        return getQuery(user, currentIndex).getAnswer().equalsIgnoreCase(answer);
    }

    public boolean hasAllAnswered(String user) {
        Predicate<Query> queryPredicate = q -> !q.isSelected();
        return Arrays.stream(getAllQueries(user))
                .anyMatch(queryPredicate.negate());
    }

    private Query[] getAllQueries(String user) {
        return dataRepository.getUserQuiz(user);
    }

    public QuizReport finishQuiz(String user, int currentIndex, String answer) {
        setSelected(user, currentIndex, answer);
        Query[] allQueries = getAllQueries(user);
        int rightAnswer = 0;
        for (Query q : allQueries) {
            for (Choice c : q.getChoices()) {
                if (c.isSelected() && c.getData().equalsIgnoreCase(q.getAnswer())) {
                    rightAnswer++;
                }

            }
        }
        return QuizReport.builder()
                .queries(allQueries)
                .totalQueries("Result: " + rightAnswer + " out of " + allQueries.length)
                .build();

    }
}
