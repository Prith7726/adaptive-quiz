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
import java.util.UUID;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuizService {

    private final DataRepository dataRepository;

    public void quiz() {
        log.warn("Am I initialized ?");
    }

    public Query getQuery(UUID uuid) {
        return getQuery(uuid, 0);
    }

    public Query getQuery(UUID uuid, int index) {
        return getAllQueries(uuid)[index];
    }

    public Query getNextQuery(UUID uuid, int currentIndex, String answer) {
        setSelected(uuid, currentIndex, answer);
        return getQuery(uuid, currentIndex + 1);
    }

    public Query getPrevQuery(UUID uuid, int currentIndex, String answer) {
        setSelected(uuid, currentIndex, answer);
        return getQuery(uuid, currentIndex - 1);
    }

    private void setSelected(UUID uuid, int currentIndex, String answer) {
        if (StringUtils.hasText(answer)) {
            for (Choice c : getQuery(uuid, currentIndex).getChoices()) {
                c.setSelected(c.getData().equalsIgnoreCase(answer));
            }
        }
    }

    public boolean hasNext(UUID uuid, int currentIndex) {
        return currentIndex + 1 < getAllQueries(uuid).length;
    }

    public boolean hasPrevious(int currentIndex) {
        return currentIndex - 1 >= 0;
    }

    public boolean checkAnswer(UUID uuid, int currentIndex, String answer) {
        setSelected(uuid, currentIndex, answer);
        return getQuery(uuid, currentIndex).getAnswer().equalsIgnoreCase(answer);
    }

    public boolean hasAllAnswered(UUID uuid) {
        Predicate<Query> queryPredicate = q -> !q.isSelected();
        return Arrays.stream(getAllQueries(uuid))
                .anyMatch(queryPredicate.negate());
    }

    private Query[] getAllQueries(UUID uuid) {
        return dataRepository.getUserQuiz(uuid);
    }

    public QuizReport finishQuiz(UUID uuid, int currentIndex, String answer) {
        setSelected(uuid, currentIndex, answer);
        Query[] allQueries = getAllQueries(uuid);
        double rightAnswer = 0;
        for (Query q : allQueries) {
            for (Choice c : q.getChoices()) {
                if (c.isSelected() && c.getData().equalsIgnoreCase(q.getAnswer())) {
                    rightAnswer++;
                }

            }
        }
        double rightPercentage = (rightAnswer / allQueries.length) * 100;
        double wrongPercentage = 100 - rightPercentage;

        return QuizReport.builder()
                .queries(allQueries)
                .rightsStyle("width: " + rightPercentage + "%")
                .wrongsStyle("width: " + wrongPercentage + "%")
                .rightsPercentage(rightPercentage + "%")
                .wrongsPercentage(wrongPercentage + "%")
                .totalQueries("Result: " + (int) rightAnswer + " correct answers out of " + allQueries.length)
                .build();

    }
}
