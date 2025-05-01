package com.adaptive.quiz.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Question table stores all the questions for the quiz.
 * This table has referential integrity with quiz and choices tables.
 */
@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    Quiz quiz;

    String subject;
    String topic;
    String question;
    int difficulty;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.ALL})
    Set<Choice> choices;


}
