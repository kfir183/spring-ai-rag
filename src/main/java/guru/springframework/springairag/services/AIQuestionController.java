package guru.springframework.springairag.services;

import guru.springframework.springairag.model.RequestQuestionResource;
import guru.springframework.springairag.model.ResponseQuestionResource;

public interface AIQuestionController {
    public ResponseQuestionResource askQuestion(RequestQuestionResource requestQuestionResource);
}
