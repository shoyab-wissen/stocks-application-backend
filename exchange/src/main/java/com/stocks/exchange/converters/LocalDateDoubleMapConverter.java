package com.stocks.exchange.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
public class LocalDateDoubleMapConverter implements AttributeConverter<Map<LocalDate, Double>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<LocalDate, Double> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            // Convert LocalDate -> String for the JSON
            Map<String, Double> stringKeyMap = new HashMap<>();
            for (Map.Entry<LocalDate, Double> e : attribute.entrySet()) {
                stringKeyMap.put(e.getKey().toString(), e.getValue());
            }
            return objectMapper.writeValueAsString(stringKeyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Map<LocalDate, Double> to JSON", e);
        }
    }

    @Override
    public Map<LocalDate, Double> convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            // Parse JSON to Map<String, Double>, then convert keys to LocalDate
            TypeReference<Map<String, Double>> typeRef = new TypeReference<>() {};
            Map<String, Double> stringKeyMap = objectMapper.readValue(dbData, typeRef);

            Map<LocalDate, Double> localDateMap = new HashMap<>();
            for (Map.Entry<String, Double> e : stringKeyMap.entrySet()) {
                localDateMap.put(LocalDate.parse(e.getKey()), e.getValue());
            }
            return localDateMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON to Map<LocalDate, Double>", e);
        }
    }
}
