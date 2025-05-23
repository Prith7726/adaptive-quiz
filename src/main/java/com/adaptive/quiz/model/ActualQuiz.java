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
 * Model class for actual quiz. When user starts a new quiz, a new record is inserted into
 * actual_quiz table with all the required fields as listed below.
 */
@Entity
@Table(name = "actual_quiz")
@Getter
@Setter
public class ActualQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_quiz_id")
    private int userQuizId;
    @Column(name = "question_id")
    private int questionId;
    private int answer;
    @Column(name = "ui_index")
    private int uiIndex;

}
