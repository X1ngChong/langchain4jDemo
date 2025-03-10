package top.bhui.lanchain4jdemo.images;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bhui.lanchain4jdemo.service.Assistant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@RequestMapping("/api/image")
@RequiredArgsConstructor
@RestController
public class ImageAPI {

    final ChatLanguageModel chatLanguageModel;
    final Assistant assistant;

    @GetMapping("/high/chat")
    public String highChat(@RequestParam(value = "message") String message) throws IOException {
        File file = new File("D:\\springcloud\\deepseek\\lanchain4j\\lanchain4jDemo\\src\\main\\java\\top\\bhui\\lanchain4jdemo\\documents\\cat.png");
        byte[] bytes = Files.readAllBytes(file.toPath());

        UserMessage from = UserMessage.from(TextContent.from(message), ImageContent.from(Base64.getEncoder().encodeToString(bytes), "image/png"));


        return chatLanguageModel.chat(List.of(from)).aiMessage().text();
    }
}
