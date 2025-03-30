package com.adaptive.quiz.repository;

import com.adaptive.quiz.model.Result;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {
    @Query("select r from Result r where r.username=:username order by r.id desc limit 5")
    List<Result> findAllByUsername(String username);
}
