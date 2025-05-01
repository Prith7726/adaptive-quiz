package com.adaptive.quiz.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Class to hold choice data retrieved from json quiz file. This class helps to keep
 * the file contents away from actual model class {@link Choice}.
 */
@Getter
@Setter
@Builder
public class ChoiceData {

    /**
     * Data of the choice.
     */
    private String data;
    /**
     * In quiz file, one of the choice for each question is delcared with isAnswer=true.
     * This value will set here.
     */
    private boolean isAnswer;
}
