package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class Query implements Serializable {

    enum Difficulty {
        EASY, INTERMEDIATE, BEGINNER;
    }
    private long id;
    private String question;
    private Choice[] choices;
    private String topic;
    private Difficulty difficulty;
    private String answer;
    private boolean selected;
}
