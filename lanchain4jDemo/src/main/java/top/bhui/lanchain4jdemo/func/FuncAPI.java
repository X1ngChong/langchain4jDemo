package top.bhui.lanchain4jdemo.func;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bhui.lanchain4jdemo.service.Assistant;
import top.bhui.lanchain4jdemo.util.JsonUtil;

import java.util.List;

@RequestMapping("/api/func")
@RestController
@RequiredArgsConstructor
public class FuncAPI {

    final Assistant assistant;

    final ChatLanguageModel chatLanguageModel;
    @GetMapping("/low/chat")
    public String lowChat(@RequestParam(value = "message") String message) {
        ToolSpecification specification = ToolSpecification.builder()
                .name("Calculator")
                .description("输入俩个书数,对这俩个数求和")
                .parameters(JsonObjectSchema.builder().addIntegerProperty("a","第一个参数")
                        .addIntegerProperty("b" ,"第二个参数")
                        .required("a","b")
                        .build())
                .build();

        ChatResponse chatResponse = chatLanguageModel.doChat(ChatRequest.builder()
                .messages(List.of(UserMessage.from(message)))
                .parameters(ChatRequestParameters.builder()
                        .toolSpecifications(specification)
                        .build()).build());

        chatResponse.aiMessage().toolExecutionRequests().forEach(toolExecutionRequest -> {
            System.out.println(toolExecutionRequest.name());
            System.out.printf(toolExecutionRequest.arguments());

            try {
                Class<?> aClass = Class.forName("top.bhui.lanchain4jdemo.func" + toolExecutionRequest.name());
                Runnable runnable =(Runnable) JsonUtil.toJsonObject(toolExecutionRequest.arguments(), aClass);
                runnable.run();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return chatResponse.aiMessage().text();
    }


    @GetMapping("/high/chat")
    public String highChat(@RequestParam(value = "message") String message) {
        return assistant.chat(message);
    }
}
