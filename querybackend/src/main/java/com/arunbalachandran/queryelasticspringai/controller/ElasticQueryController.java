package com.arunbalachandran.queryelasticspringai.controller;

import com.arunbalachandran.queryelasticspringai.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/elastic")
public class ElasticQueryController {

    @Autowired
    private PromptService promptService;

    // TODO: change to JSON
    @GetMapping("/query")
    public String queryElastic(@RequestParam String prompt) {
        return promptService.processPrompt(prompt);
    }
}
