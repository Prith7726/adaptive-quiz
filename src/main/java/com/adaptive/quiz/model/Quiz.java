package com.adaptive.quiz.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Quiz table to store quiz data. This has referential integrity with questions table.
 */
@Entity
@Getter
@Setter
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    @Column(name = "file_name")
    String filename;

    @OneToMany(mappedBy = "quiz", cascade = {CascadeType.ALL})
    Set<Question> questions;

}
