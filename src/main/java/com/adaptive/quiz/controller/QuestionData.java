package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holder class for Questions from json file.
 */
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
