package com.arunbalachandran.queryelasticspringai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElasticsearchService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    private final String INDEX_NAME = "connector.public.orders";

    public String getOrderMapping() {
        return restTemplate.getForObject(
            elasticsearchUrl + "/" + INDEX_NAME + "/_mapping",
            String.class
        );
    }

    public String search(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(query, headers);
        return restTemplate.postForObject(
                elasticsearchUrl + "/" + INDEX_NAME + "/_search",
            request,
            String.class
        );
    }
} 