package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.Choice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceRepository extends CrudRepository<Choice, Integer> {
}
