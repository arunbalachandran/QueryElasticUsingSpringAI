package com.arunbalachandran.queryelasticspringai.controller;

import com.arunbalachandran.queryelasticspringai.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/elastic")
public class ElasticQueryController {

    @Autowired
    private PromptService promptService;

    @PostMapping("/query")
    public ResponseEntity<String> queryElastic(@RequestBody String prompt) {
        return ResponseEntity.ok(promptService.processPrompt(prompt));
    }
}
