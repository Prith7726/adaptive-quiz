package com.adaptive.quiz.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class FirstController {

    @GetMapping("/helloWorld")
    public String hello() {
        return "Well done... you are here";
    }

    @GetMapping("/quiz/challenge")
    @ResponseBody
    public String challenge() {
        return """
                 {
                      "id":1,
                      "question": "What is 5+2?",
                      "choices" : ["5", "2", "7"]
                    }
                """;
    }


    @GetMapping("/quiz/answer")
    @ResponseBody
    public String answer(@RequestParam(name = "id") int id, @RequestParam(name = "answer") String value) {
        return "7".equals(value) ?
                """
                  {
                    "answer" : "Correct"
                  }
                        """
                :
                """
                    {
                        "answer": "Wrong"
                        }
                """;
    }

}
