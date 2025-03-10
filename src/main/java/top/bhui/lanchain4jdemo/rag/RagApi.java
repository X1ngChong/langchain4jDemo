package top.bhui.lanchain4jdemo.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bhui.lanchain4jdemo.service.Assistant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/api/rag")
@RestController
@RequiredArgsConstructor
public class RagApi {

    final Assistant assistant;
    @GetMapping("/high/chat")
    public String chat(@RequestParam(value = "message") String message) {
     return assistant.chat(message);
    }


    final EmbeddingStore<TextSegment> embeddingStore;

    final EmbeddingModel embeddingModel;
    @GetMapping("/load")
    public String load(){
        //加载文件
        List<Document> document = FileSystemDocumentLoader.loadDocuments("D:\\springcloud\\deepseek\\lanchain4j\\lanchain4jDemo\\src\\main\\java\\top\\bhui\\lanchain4jdemo\\documents");
       // EmbeddingStoreIngestor.ingest(document,embeddingStore);
        EmbeddingStoreIngestor.builder().embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel)
                .documentSplitter(new DocumentByLineSplitter(30,20))
                .build().ingest(document);
        return "SUCCESS";
    }


}
