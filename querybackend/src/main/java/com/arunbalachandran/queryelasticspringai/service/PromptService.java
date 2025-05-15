package com.arunbalachandran.queryelasticspringai.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            
            Based on the above mapping, convert the following user query into an elasticsearch query as a JSON. We need to be able to use the response as part of an API call to Elastic.
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
    public String processPrompt(String userQuery) {
        String fullPrompt = basePrompt + "\nUser query: " + userQuery;
        log.info("Prompt being used: {}", fullPrompt);
        String elasticQuery = chatModel.call(fullPrompt);
        log.info("Elastic query: {}", elasticQuery);
        return elasticsearchService.search(elasticQuery);
    }
}
