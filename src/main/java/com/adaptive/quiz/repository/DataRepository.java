package com.adaptive.quiz.repository;

import com.adaptive.quiz.controller.Query;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataRepository {

    ConcurrentHashMap<String, Query[]> userQuizTable = new ConcurrentHashMap<>();

    public String register(String userName, Query[] queries) {
        userQuizTable.put(userName, queries);
        return userName;
    }

    public Query[] getUserQuiz(String userName) {
        return userQuizTable.get(userName);
    }

    public void unRegister(String user) {
        userQuizTable.remove(user);
    }
}
