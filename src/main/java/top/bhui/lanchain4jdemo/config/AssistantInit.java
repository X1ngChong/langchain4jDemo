package top.bhui.lanchain4jdemo.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.bhui.lanchain4jdemo.func.HighLevelCalculator;
import top.bhui.lanchain4jdemo.service.Assistant;

@Configuration
@RequiredArgsConstructor
public class AssistantInit {

    final ChatLanguageModel chatLanguageModel;

//    @Bean
//    public  EmbeddingStore<TextSegment> initEmbeddingStore() {
//        return new InMemoryEmbeddingStore<>();
//    }

    @Bean
    public Assistant init(EmbeddingStore<TextSegment> embeddingStore, SearchApiWebSearchEngine engine) {
        return AiServices.builder(Assistant.class)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .tools(new HighLevelCalculator(),new WebSearchTool(engine))
                .chatLanguageModel(chatLanguageModel).build();
    }

//    @Bean
//    public Assistant init(SearchApiWebSearchEngine engine) {
//        return AiServices.builder(Assistant.class)
//                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
//                .tools(new HighLevelCalculator(),new WebSearchTool(engine))
//                .chatLanguageModel(chatLanguageModel).build();
//    }
}
