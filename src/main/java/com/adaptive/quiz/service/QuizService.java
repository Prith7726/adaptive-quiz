package com.adaptive.quiz.service;

import com.adaptive.quiz.controller.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        return queries[0];
    }

    public Query getNextQuery(int currentIndex) {
        return queries[currentIndex + 1];
    }

    public Query getPrevQuery(int currentIndex) {
        return queries[currentIndex - 1];
    }

    public boolean hasNext(int currentIndex) {
        return currentIndex + 1 < queries.length;
    }

    public boolean hasPrevious(int currentIndex) {
        return currentIndex - 1 >= 0;
    }
}
