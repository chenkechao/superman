package com.keke.superman.esb.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.springside.modules.mapper.JsonMapper;

public class JsonMapperHolder
{
    public static final ObjectMapper objectMapper;
    public static final JsonMapper jsonMapper = new JsonMapper();

    static
    {
        objectMapper = jsonMapper.getMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.setDateFormat(new ISO8601DateFormat());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule localSimpleModule = new SimpleModule();
        localSimpleModule.addSerializer(Long.class, new Long2StringSerializer());
        objectMapper.registerModule(localSimpleModule);
    }

    public static final ObjectNode createObjectNode()
    {
        return objectMapper.createObjectNode();
    }

    public static final <T> T convert(Object value, Class<T> clazz)
    {
        return (T)objectMapper.convertValue(value, clazz);
    }

    public static final JsonNode convert(Object value)
    {
        return (JsonNode)convert(value, JsonNode.class);
    }
}
