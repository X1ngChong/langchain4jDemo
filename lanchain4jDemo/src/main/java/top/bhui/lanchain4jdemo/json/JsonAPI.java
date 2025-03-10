package top.bhui.lanchain4jdemo.json;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bhui.lanchain4jdemo.service.Assistant;

import java.util.List;

@RequestMapping("/api/json")
@RestController
@RequiredArgsConstructor
public class JsonAPI {

    final ChatLanguageModel chatLanguageModel;
    final Assistant assistant;



    @GetMapping("/high/chat")
    public String highChat(@RequestParam(value = "message") String message) {

        PersonService personService = AiServices.create(PersonService.class, chatLanguageModel);
        Person person = personService.extractPerson(message);

        return person.toString();
    }

    @GetMapping("/low/chat")
    public String lowChat(@RequestParam(value = "message") String message) {
        ResponseFormat build = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .rootElement(JsonObjectSchema.builder()
                                .addIntegerProperty("age", "the person age")
                                .addIntegerProperty("weight", "the person weight")
                                .required("age", "weight")
                                .build())
                        .build())
                .build();

        ChatResponse chat = chatLanguageModel.chat(ChatRequest.builder()
                .messages(List.of(UserMessage.from(message)))
                .parameters(ChatRequestParameters.builder()
                        .responseFormat(build)
                        .build())
                .build());

        return chat.aiMessage().text();
    }
}
