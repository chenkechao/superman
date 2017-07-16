package com.keke.superman.esb.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class Long2StringSerializer
        extends JsonSerializer<Long>
{
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        if (value.longValue() > 999999999999999L) {
            jgen.writeString(String.valueOf(value));
        } else {
            jgen.writeNumber(value.longValue());
        }
    }
}
