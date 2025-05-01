package com.adaptive.quiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Result model. Stores all the result details for the user quiz. This data is used to render results in
 * user interface.
 */
@Entity
@Table(name = "results")
@Getter
@Setter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quizId;
    @Column(name = "user_quiz_id")
    private int userQuizId;
    @Column(name = "quiz_name")
    private String quizName;
    private String username;
    @Column(name = "total")
    private int totalQuestions;
    @Column(name = "answered")
    private int answeredQuestions;
    @Column(name = "un_answered")
    private int unAnsweredQuestions;
    @Column(name = "right_answers")
    private int rightAnswers;
    @Column(name = "wrong_answers")
    private int wrongAnswers;
    @Column(name = "percentage")
    private double percentageCorrectAnswers;
    private int level1;
    private int level2;
    private int level3;
    private int level4;
    private int level5;

    public int[] getLevels() {
        return new int[]{level1, level2, level3, level4, level5};
    }
}
