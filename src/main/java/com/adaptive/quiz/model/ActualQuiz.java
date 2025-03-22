package com.adaptive.quiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "actual_quiz")
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
