package guru.springframework.springairag.controllers;

import guru.springframework.springairag.model.RequestQuestionResource;
import guru.springframework.springairag.model.ResponseQuestionResource;

public interface AIQuestionService {
    public ResponseQuestionResource askQuestion(RequestQuestionResource question);

}
