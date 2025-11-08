package guru.springframework.springairag.services;

import guru.springframework.springairag.controllers.AIQuestionService;
import guru.springframework.springairag.model.RequestQuestionResource;
import guru.springframework.springairag.model.ResponseQuestionResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AIQuestionControllerImpl implements AIQuestionController{

    private final AIQuestionService aiQuestionService;


    @PostMapping("/ask-question")
    public ResponseQuestionResource askQuestion(@RequestBody RequestQuestionResource requestQuestionResource) {

        return aiQuestionService.askQuestion(requestQuestionResource);
    }
}
