package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class QuizReport {

    private String totalQueries;
    private Query[] queries;
    private String rightsStyle;
    private String wrongsStyle;
    private String rightsPercentage;
    private String wrongsPercentage;


}
