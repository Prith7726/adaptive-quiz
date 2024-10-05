package com.adaptive.quiz.service;

import com.adaptive.quiz.controller.RegisterResponse;
import com.adaptive.quiz.model.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final RegisterRepository repository;

    public RegisterResponse register(String name) {
        UUID uuid = UUID.randomUUID();
        String actualName = StringUtils.hasText(name) ? name : uuid.toString();
        repository.register(uuid, actualName);
        return new RegisterResponse(uuid, actualName);
    }
}
