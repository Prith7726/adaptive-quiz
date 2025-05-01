package com.adaptive.quiz.service;

import com.adaptive.quiz.model.QuizOverview;
import com.adaptive.quiz.repository.QuizOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Service class for list all the Quiz.
 */
@Service
@RequiredArgsConstructor
public class QuizListService {

    private final QuizOverviewRepository quizOverviewRepository;

    /**
     * Retrieves all the quiz from quiz overview table and returns it to caller.
     *
     * @return
     */
    public List<QuizOverview> getAllQuiz() {
        return StreamSupport.stream(quizOverviewRepository.findAll().spliterator(), false)
                .toList();
    }
}
