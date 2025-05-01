package com.adaptive.quiz.controller;

import lombok.Getter;
import lombok.Setter;

/**
 * Model data class to hold question choices.
 */
@Getter
@Setter
public class Choice {
    /**
     * Holds choice id value.
     */
    private int choice;
    /**
     * Holds choice data.
     */
    private String data;
    /**
     * Holds true if choice is selected, otherwise false.
     */
    private boolean selected;
}
