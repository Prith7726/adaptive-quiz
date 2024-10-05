package com.adaptive.quiz.controller;

import java.util.UUID;

public record DeRegisterResponse(String message, Status status) {
    enum Status {
        DE_REGISTER_SUCCESS, DE_REGISTER_FAILED;
    }
}
