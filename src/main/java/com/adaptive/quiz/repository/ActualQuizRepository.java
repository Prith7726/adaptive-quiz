package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.ActualQuiz;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ActualQuizRepository extends CrudRepository<ActualQuiz, Integer> {
    @Query("select a.questionId from ActualQuiz a where a.userQuizId=:userQuizId")
    List<Integer> findQuestionsIdByUserQuizId(int userQuizId);

    @Transactional
    @Modifying
    @Query(value = "delete from ActualQuiz a where a.userQuizId in :userQuizId")
    void deleteAllByUserQuizId(List<Integer> userQuizId);

    ActualQuiz findActualQuizByUiIndex(int uiIndex);

    List<ActualQuiz> findAllByUserQuizId(int userQuizId);

    ActualQuiz findActualQuizByUserQuizIdAndUiIndex(int userQuizId, int uiIndex);
}
