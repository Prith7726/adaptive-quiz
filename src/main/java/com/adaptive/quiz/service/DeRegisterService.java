package com.adaptive.quiz.service;

import com.adaptive.quiz.model.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeRegisterService {

    private final RegisterRepository repository;

    public String deregister(UUID key) {
        return repository.deregister(key);
    }
}
