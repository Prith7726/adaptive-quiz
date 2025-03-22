package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.ActualQuiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActualQuizRepository extends CrudRepository<ActualQuiz, Integer> {
    @Query("select a.questionId from actual_quiz a where a.userQuizId=:userQuizId")
    List<Integer> findQuestionsIdByUserQuizId(int userQuizId);
}
