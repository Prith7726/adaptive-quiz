package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class QuizData implements Serializable {

    private String quizName;
    private QuestionData[] questions;

}
