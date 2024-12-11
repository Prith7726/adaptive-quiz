package com.adaptive.quiz.repository;

import com.adaptive.quiz.controller.Query;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataRepository {

    ConcurrentHashMap<UUID, Query[]> userQuizTable = new ConcurrentHashMap<>();

    public UUID register(UUID uuid, Query[] queries) {
        userQuizTable.put(uuid, queries);
        return uuid;
    }

    public Query[] getUserQuiz(UUID uuid) {
        return userQuizTable.get(uuid);
    }

    public void unRegister(UUID uuid) {
        userQuizTable.remove(uuid);
    }
}
