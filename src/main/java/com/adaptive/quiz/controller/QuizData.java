package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Class to hold Quiz data retrieved from json quiz file. This class helps to keep
 * the file contents away from actual model class {@link com.adaptive.quiz.model.Quiz}.
 */
@Getter
@Setter
@Builder
public class QuizData implements Serializable {

    private String quizName;
    private QuestionData[] questions;

}
