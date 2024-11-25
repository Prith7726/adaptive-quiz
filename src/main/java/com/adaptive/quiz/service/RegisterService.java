package com.adaptive.quiz.service;

import com.adaptive.quiz.controller.Query;
import com.adaptive.quiz.controller.RegisterResponse;
import com.adaptive.quiz.model.RegisterRepository;
import com.adaptive.quiz.repository.DataRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class RegisterService {

    private final RegisterRepository repository;
    private final DataRepository dataRepository;
    private final Query[] queries;

    public RegisterService(RegisterRepository repository, DataRepository dataRepository,
                           @Qualifier("allQueries") Query[] queries) {
        this.repository = repository;
        this.dataRepository = dataRepository;
        this.queries = queries;
    }

    public RegisterResponse register(String name) {
        UUID uuid = UUID.randomUUID();
        String actualName = StringUtils.hasText(name) ? name : uuid.toString();
        repository.register(uuid, actualName);
        dataRepository.register(actualName, queries);
        return new RegisterResponse(uuid, actualName);
    }
}
