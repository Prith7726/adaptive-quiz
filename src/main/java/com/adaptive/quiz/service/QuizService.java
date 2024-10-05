package com.adaptive.quiz.service;

import com.adaptive.quiz.controller.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.random.RandomGenerator;

@Service
@Slf4j
public class QuizService {

    private final Query[] queries;

    public QuizService(@Qualifier("allQueries") Query[] queries) {
        this.queries = queries;
    }

    public void quiz() {
        log.warn("Am I initialized ?");
    }

    public Query getQuery() {
        return queries[RandomGenerator.getDefault().nextInt(0, 10)];
    }

    public Query getQuery(int currentIndex) {
        int index = getNextIndex(currentIndex);
        return queries[index];
    }

    private int getNextIndex(int currentIndex) {
        int i = RandomGenerator.getDefault().nextInt(0, 10);
        if (i != currentIndex) {
            return i;
        }
        return getNextIndex(currentIndex);
    }
}
