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
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Configuration class for adding new quizzes from resource folder.
 */
@Configuration
@RequiredArgsConstructor
public class AddNewQuizConfig {

    private final QuizRepository quizRepository;
    private final QuizOverviewRepository quizOverviewRepository;

    /**
     * This is spring config bean method, spring boot application triggers this method when the server starts up.
     * This method retrieves files from resources/quizzes folder and add each quiz file contents into
     * database table.
     * If the quiz is already present in the table, then it will be ignored.
     * For ease of use, we currently use only 3 files, declared in this method. If new file is added, then
     * filename should be included into files array in code below. This also help to ignore any existing file which
     * is not required to be included in quiz.
     * <p>
     * This method updates table Quiz and QuizRepository.
     * Quiz: All the quiz questions, choices are inserted.
     * QuizOverview: Overview of the quiz like quiz name, quiz id, subjects, topics, number of questions are inserted.
     */
    @Bean
    public String getQueries() {
        String[] files = {"queries_db1.json", "queries_db2.json", "queries_db3.json"};
        for (String file : files) {

            Quiz byFilename = quizRepository.findByFilename(file);
            if (byFilename != null) {
                continue;
            }
            Resource resource = new ClassPathResource("quizzes/" + file);

            QuizData quizData;
            try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
                quizData = (new Gson().fromJson(reader, QuizData.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Quiz quiz = new Quiz();
            quiz.setName(quizData.getQuizName());
            quiz.setFilename(file);
            quiz.setQuestions(getQuestions(quiz, quizData));
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

    /**
     * Retrieves questions from the given quiz data and maps data into Question object.
     * Returns a Set of Question which contains only unique values.
     *
     * @param quiz     {@link Quiz}
     * @param quizData {@link QuizData}
     * @return questions {@link Set<Question>}
     */
    private Set<Question> getQuestions(Quiz quiz, QuizData quizData) {
        return Arrays.stream(quizData.getQuestions())
                .map(q -> mapToQuestion(quiz, q))
                .collect(Collectors.toSet());
    }

    /**
     * Handy method to map QuestionData contents into Question object.
     *
     * @param quiz {@link Quiz}
     * @param data {@link QuizData}
     * @return question {@link Question}
     */
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

    /**
     * This method retrieves the choices for the question, maps it into Choice object.
     *
     * @param question {@link Question}
     * @param data     {@link QuestionData}
     * @return choices {@link Set<Choice>}
     */
    private Set<Choice> getChoices(Question question, QuestionData data) {
        return Arrays.stream(data.getChoices())
                .map(c -> mapToChoice(question, c))
                .collect(Collectors.toSet());
    }

    /**
     * Mapper method to map ChoiceData to Choice object.
     *
     * @param question {@link Question}
     * @param data     {@link ChoiceData}
     * @return choice {@link Choice}
     */
    private Choice mapToChoice(Question question, ChoiceData data) {
        Choice choice = new Choice();
        choice.setQuestion(question);
        choice.setData(data.getData());
        choice.setAnswer(data.isAnswer());
        return choice;
    }
}
