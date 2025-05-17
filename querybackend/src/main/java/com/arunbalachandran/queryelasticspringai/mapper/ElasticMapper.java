package com.arunbalachandran.queryelasticspringai.mapper;

import com.arunbalachandran.queryelasticspringai.dto.OrderDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ElasticMapper {

    private final static String HITS = "hits";
    private final static String SOURCE = "_source";

    public static List<OrderDTO> mapToOrderDTO(Map<String, Object> esResponse) {
        Map<String, Object> hitsMap = (Map<String, Object>) esResponse.get(HITS);
        var hitsList = (java.util.List<Map<String, Object>>) hitsMap.get(HITS);

        if (hitsList.isEmpty()) {
            return List.of();
        }

        return hitsList.stream()
                .map(
                        val -> (Map<String, Object>) val.get(SOURCE)
                ).map(
                        source -> OrderDTO.builder()
                                        .id(UUID.fromString((String) source.get("id")))
                                        .productName((String) source.get("product_name"))
                                        .productQty((Integer) source.get("product_qty"))
                                        .productPrice(((Number) source.get("product_price")).doubleValue())
                                        .productDescription((String) source.get("product_description"))
                                        .createdTime(toLocalDateTime((Long) source.get("created_time")))
                                        .updatedTime(toLocalDateTime((Long) source.get("updated_time")))
                                        .build()
                ).toList();
    }

    private static LocalDateTime toLocalDateTime(Long timestampMillis) {
        return Instant.ofEpochMilli(timestampMillis).atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
