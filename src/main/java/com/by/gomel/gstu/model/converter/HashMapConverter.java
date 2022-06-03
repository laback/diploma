package com.by.gomel.gstu.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, String>, String> {

    private static final Logger logger = LoggerFactory.getLogger(HashMapConverter.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> stringStringMap) {
        String databaseColumn = null;

        try{
            databaseColumn = objectMapper.writeValueAsString(stringStringMap);
        } catch (JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return databaseColumn;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        Map<String, String> entityColumn = null;

        try{
            entityColumn = objectMapper.readValue(s, Map.class);
        } catch (JsonProcessingException e) {
            logger.error("JSON reading error", e);
        }

        return entityColumn;
    }
}
