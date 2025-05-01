package com.adaptive.quiz.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * This model is for quiz_overview table. This stores all the details for each quiz.
 */
@Getter
@Setter
@Entity(name = "quiz_overview")
public class QuizOverview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quiz_id")
    private int quizId;
    @Column(name = "quiz_name")
    private String quizName;

    private String subjects;
    private String topics;
    private String levels;
    @Column(name = "question_count")
    private int questionCount;

}
