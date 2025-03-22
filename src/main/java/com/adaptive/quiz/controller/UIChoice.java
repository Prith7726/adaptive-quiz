package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UIChoice {
    private int id;
    private String data;
    private boolean selected = false;
}
