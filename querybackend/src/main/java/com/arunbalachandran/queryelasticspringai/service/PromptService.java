package com.arunbalachandran.queryelasticspringai.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    @Autowired
    private ChatModel chatModel;

    public String processPrompt(String prompt) {
        return chatModel.call(prompt);
    }

}
