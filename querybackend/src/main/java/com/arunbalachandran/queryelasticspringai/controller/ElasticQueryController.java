package com.arunbalachandran.queryelasticspringai.controller;

import com.arunbalachandran.queryelasticspringai.dto.OrderDTO;
import com.arunbalachandran.queryelasticspringai.dto.QueryDTO;
import com.arunbalachandran.queryelasticspringai.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/v1/elastic")
public class ElasticQueryController {

    @Autowired
    private PromptService promptService;

    /**
     * Get orders based on a natural language query by the user. The dynamically generated query is used to fetch the
     * orders from elasticsearch.
     *
     * @param userQuery
     * @return
     */
    @PostMapping("/query")
    public ResponseEntity<List<OrderDTO>> queryElastic(@RequestBody QueryDTO userQuery) {
        return ResponseEntity.ok(promptService.processPrompt(userQuery.getQuery()));
    }
}
