package com.adaptive.quiz.controller;


import com.adaptive.quiz.service.QuizListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring {@link Controller} class for home.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final QuizListService quizListService;

    /**
     * This method is a {@link GetMapping} for /home resource.
     * This is called when user successfully logged in.
     * This method pulls available quiz from quiz table and set into model object for
     * view to render and present to the user.
     * Returns a constant string 'home', which is then set to 'home.html' by view resolver.
     *
     * @param model {@link Model}
     * @return home {@link String}
     */
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("quizList", quizListService.getAllQuiz());
        return "home";
    }
}
