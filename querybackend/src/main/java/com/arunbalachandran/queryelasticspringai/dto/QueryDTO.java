package com.arunbalachandran.queryelasticspringai.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class QueryDTO {
    String query;
}