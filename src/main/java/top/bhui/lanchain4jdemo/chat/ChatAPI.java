package top.bhui.lanchain4jdemo.chat;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bhui.lanchain4jdemo.service.Assistant;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ChatAPI {

    final ChatLanguageModel chatLanguageModel;

    @GetMapping("/low/chat")
    public String lowChat(@RequestParam(value = "message") String message) {

       return chatLanguageModel.chat(List.of(SystemMessage.systemMessage("假如你是特朗普,接下来请以特朗普的语气来对话"),UserMessage.userMessage(message))).aiMessage().text();

       // return chatLanguageModel.chat(UserMessage.from(message)).aiMessage().text();
    }

    final Assistant assistant;

    @GetMapping("/high/chat")
    public String highChat(@RequestParam(value = "message") String message) {
        return assistant.chat(message);
    }
}
