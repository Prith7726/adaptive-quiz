package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.Quiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends CrudRepository<Quiz, Integer> {
    @Query("select q from Quiz q where q.filename=:fileName")
    Quiz findByFilename(@Param("fileName") String fileName);
}
