package com.adaptive.quiz.controller;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class to hold question data to render in user interface.
 */
@Getter
@Setter
@Builder
public class UIQuestion {

    private int quizId;
    private int actualQuizId;
    private int questionId;

    private String question;
    private String subject;
    private String topic;
    private int difficulty;
    private List<UIChoice> choices;
    private int answer;
    private int uiIndex;
    private int totalQuestions;
}
