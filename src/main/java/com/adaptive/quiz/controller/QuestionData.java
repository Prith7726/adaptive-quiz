package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionData {
    String subject;
    String topic;
    String question;
    int difficulty;
    ChoiceData[] choices;
}
