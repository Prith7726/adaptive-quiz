package com.adaptive.quiz.controller;

import java.io.Serializable;
import java.util.UUID;

public record RegisterResponse(UUID uuid, String name) implements Serializable {
}
