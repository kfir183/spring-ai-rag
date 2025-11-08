package guru.springframework.springairag.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Slf4j
@Configuration
public class VectorStoreConfig {

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel, VectorStoreProperties vectorStoreProperties) {
        // Create vector store based on embedding model
        SimpleVectorStore store =  SimpleVectorStore.builder(embeddingModel).build();
        // Get vector store file
        System.out.println("vector store path:" + vectorStoreProperties.getVectorStorePath());
        File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());

        if (vectorStoreFile.exists()) {
            // Load existing vector store
            log.debug("Loading existing Vector Store from: " + vectorStoreFile.getAbsolutePath());
            store.load(vectorStoreFile);
        }  else {
            log.debug("Loading new Vector Store");
            vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
                try {
                    log.debug("Loading Document: " + document.getFilename());

                    // get document from resource
                    TikaDocumentReader documentReader = new TikaDocumentReader(document);
                    List<Document> docs = documentReader.get();

                    // Split Documents into Chunks
                    TextSplitter txtSplitter = new TokenTextSplitter();
                    List<Document> splitDocs = txtSplitter.apply(docs);

                    // add to vector store
                    store.add(splitDocs);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            store.save(vectorStoreFile);

        }
        return store;
    }
}
