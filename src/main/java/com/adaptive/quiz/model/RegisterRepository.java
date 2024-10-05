package com.adaptive.quiz.model;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegisterRepository {
    private final ConcurrentHashMap<UUID, String> registers = new ConcurrentHashMap<>();

    public void register(UUID key, String name) {
        registers.put(key, name);
    }

    public String deregister(UUID key) {
        return registers.remove(key);
    }
}
