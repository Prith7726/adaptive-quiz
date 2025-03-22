package com.adaptive.quiz.config;

import com.adaptive.quiz.controller.ChoiceData;
import com.adaptive.quiz.controller.QuestionData;
import com.adaptive.quiz.controller.QuizData;
import com.adaptive.quiz.model.Choice;
import com.adaptive.quiz.model.Question;
import com.adaptive.quiz.model.Quiz;
import com.adaptive.quiz.model.QuizOverview;
import com.adaptive.quiz.repository.QuizOverviewRepository;
import com.adaptive.quiz.repository.QuizRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
@RequiredArgsConstructor
public class AddNewDataService {

    private final QuizRepository quizRepository;
    private final QuizOverviewRepository quizOverviewRepository;

    @Bean
    public String getQueries() {
        String[] files = {"queries_db1.json", "queries_db2.json", "queries_db3.json"};
        for (String file : files) {


            Quiz byFilename = quizRepository.findByFilename(file);
            if (byFilename != null) {
                continue;
            }
            Resource resource = new ClassPathResource("quizzes/" + file);

            QuizData combinedData;
            try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
                combinedData = (new Gson().fromJson(reader, QuizData.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Quiz quiz = new Quiz();
            quiz.setName(combinedData.getQuizName());
            quiz.setFilename(file);
            quiz.setQuestions(getQuestions(quiz, combinedData));
            Quiz savedQuiz = quizRepository.save(quiz);

            QuizOverview overview = new QuizOverview();
            overview.setQuizId(savedQuiz.getId());
            overview.setQuizName(savedQuiz.getName());
            overview.setSubjects(savedQuiz.getQuestions().stream()
                    .map(Question::getSubject)
                    .distinct()
                    .sorted()
                    .collect(Collectors.joining(",")));
            overview.setTopics(savedQuiz.getQuestions().stream()
                    .map(Question::getTopic)
                    .distinct()
                    .sorted()
                    .collect(Collectors.joining(",")));
            overview.setLevels(savedQuiz.getQuestions().stream()
                    .map(Question::getDifficulty)
                    .distinct()
                    .sorted()
                    .map(d -> "" + d)
                    .collect(Collectors.joining(",")));
            overview.setQuestionCount(savedQuiz.getQuestions().size());

            quizOverviewRepository.save(overview);
        }


        return "done";
    }

    private Set<Question> getQuestions(Quiz quiz, QuizData combinedData) {
        return Arrays.stream(combinedData.getQuestions())
                .map(q -> mapToQuestion(quiz, q))
                .collect(Collectors.toSet());
    }

    private Question mapToQuestion(Quiz quiz, QuestionData data) {
        Question question = new Question();
        question.setQuiz(quiz);
        question.setSubject(data.getSubject());
        question.setTopic(data.getTopic());
        question.setQuestion(data.getQuestion());
        question.setDifficulty(data.getDifficulty());
        question.setChoices(getChoices(question, data));
        return question;
    }

    private Set<Choice> getChoices(Question question, QuestionData data) {
        return Arrays.stream(data.getChoices())
                .map(c -> mapToChoice(question, c))
                .collect(Collectors.toSet());
    }

    private Choice mapToChoice(Question q, ChoiceData data) {
        Choice c = new Choice();
        c.setQuestion(q);
        c.setData(data.getData());
        c.setAnswer(data.isAnswer());
        return c;
    }
}
