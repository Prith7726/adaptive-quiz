package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.QuizOverview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizOverviewRepository extends CrudRepository<QuizOverview, Integer> {
}
