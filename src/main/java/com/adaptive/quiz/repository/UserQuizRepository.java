package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.UserQuiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizRepository extends CrudRepository<UserQuiz, Integer> {

    @Query("select u from UserQuiz u where u.quizId=:quizId and u.username=:username")
    UserQuiz findByIdAndUsername(@Param("quizId") int quizId, @Param("username") String username);

    List<UserQuiz> findUserQuizByUsername(String username);
}
