package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class QuizReport {

    private int totalQueries;
    private int rightAnswers;
    private int wrongAnswers;

}
