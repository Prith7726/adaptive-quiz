package com.adaptive.quiz.service;

import com.adaptive.quiz.controller.Query;
import com.adaptive.quiz.controller.RegisterResponse;
import com.adaptive.quiz.model.RegisterRepository;
import com.adaptive.quiz.repository.DataRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
public class RegisterService {

    private final RegisterRepository repository;
    private final DataRepository dataRepository;
    private final Resource resource;

    public RegisterService(RegisterRepository repository,
                           DataRepository dataRepository,
                           @Value("classpath:queries.json") Resource resource) {
        this.repository = repository;
        this.dataRepository = dataRepository;
        this.resource = resource;
    }

    public RegisterResponse register(String name) {
        UUID uuid = UUID.randomUUID();
        String actualName = StringUtils.hasText(name) ? name : uuid.toString();
        repository.register(uuid, actualName);

        dataRepository.register(uuid, getQueries());
        return new RegisterResponse(uuid, actualName);
    }

    public Query[] getQueries() {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return new Gson().fromJson(reader, Query[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deRegister(UUID uuid) {
        dataRepository.unRegister(uuid);
    }
}
