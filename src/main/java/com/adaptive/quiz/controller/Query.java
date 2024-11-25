package com.adaptive.quiz.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Query {

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
