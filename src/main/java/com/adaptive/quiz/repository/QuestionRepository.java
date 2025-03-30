package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {
    List<Question> findAllByQuizId(Integer id);
}
