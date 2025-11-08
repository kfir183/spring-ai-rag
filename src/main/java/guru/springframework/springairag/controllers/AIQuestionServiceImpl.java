package guru.springframework.springairag.controllers;

import guru.springframework.springairag.model.RequestQuestionResource;
import guru.springframework.springairag.model.ResponseQuestionResource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIQuestionServiceImpl implements AIQuestionService{

    private final ChatModel chatModel;
    private final SimpleVectorStore vectorStore;

    @Value("classpath:templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Override
    public ResponseQuestionResource askQuestion(RequestQuestionResource question) {
        // Search in the Vector Store
        List<Document> documents = vectorStore.similaritySearch(SearchRequest
                .builder()
                .query(question.question())
                .topK(5).build());

        // get Documents content
        List<String> documentsContent = documents.stream()
                .map(Document::getText)
                .toList();


        // Create Prompt and attach the documents from the vector store
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
                "input", question.question(),
                "documents", String.join("\n\n", documentsContent)
        ));

        documentsContent.forEach(System.out::println);

        // Call the Chat Model
        ChatResponse response = chatModel.call(prompt);

        return new ResponseQuestionResource(response.getResult().getOutput().getContent());
    }
}
