package com.pandora.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pandora.api.exceptions.rest.BadRequestRestException;
import kong.unirest.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperImp implements ObjectMapper {

    com.fasterxml.jackson.databind.ObjectMapper mapper
            = new com.fasterxml.jackson.databind.ObjectMapper();

    @SneakyThrows
    @Override
    public String writeValue(Object value) {
        try {
            String object = mapper.writeValueAsString(value);
            log.info("sent object : " + object.replaceAll("\\R", " "));
            return object;
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public <T> T readValue(String value, Class<T> valueType) {
        log.info("received object : " + value.replaceAll("\\R", " "));
        if (value.contains("errCode") || value.contains("errMsg")) {
            log.warn("response contains error : " + value);
            throw new BadRequestRestException("failed to parse response");
        }
        try {
            return mapper.readValue(value, valueType);
        } catch (JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }
    }
}
