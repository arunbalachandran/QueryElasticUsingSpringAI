package com.arunbalachandran.queryelasticspringai.service;

import com.arunbalachandran.queryelasticspringai.dto.OrderDTO;
import com.arunbalachandran.queryelasticspringai.mapper.ElasticMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PromptService {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private ElasticsearchService elasticsearchService;

    private String basePrompt;

    @PostConstruct
    public void init() {
        String mapping = elasticsearchService.getOrderMapping();
        this.basePrompt = """
            I need you to convert natural language user queries into elasticsearch queries. This is a mapping of the Order index in Elasticsearch.
            ```
            %s
            ```
            
            Based on the above mapping, convert the following user query into an elasticsearch query as a JSON.
            Try to understand the semantics of the query & generate the query based on the context given.
            If the query mentions a concept, then try to understand the semantics of what is being asked and use that while generating the query.
            We need to be able to use the response as part of an API call to Elastic.
            Output the mapping without formatting, no markdown. Just the query in JSON format. No triple ticks to delimit the query.
            """.formatted(mapping);
    }

    /**
     * Process the natural language userQuery and convert it prompt that is fed to OpenAPI. The LLM in turn, converts
     * the userQuery into an elasticsearch query that can be used to fetch the results.
     *
     * @param userQuery
     * @return
     */
    public List<OrderDTO> processPrompt(String userQuery) {
        String fullPrompt = basePrompt + "\nUser query: " + userQuery;
        log.info("Prompt being used: {}", fullPrompt);
        ChatResponse chatResponse = chatModel.call(
                new Prompt(
                        fullPrompt,
                        OpenAiChatOptions.builder()
                                .model("gpt-4o")
                                .temperature(1.0)
                                .build()
                )
        );
        String elasticQuery = chatResponse.getResult().getOutput().getText();
        log.info("Elastic query: {}", elasticQuery);
        Map<String, Object> response = elasticsearchService.search(elasticQuery);
        return ElasticMapper.mapToOrderDTO(response);
    }
}
