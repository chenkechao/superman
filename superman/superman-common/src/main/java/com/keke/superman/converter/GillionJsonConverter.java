package com.keke.superman.converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

import com.keke.superman.esb.util.ResultUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;

public class GillionJsonConverter
        extends MappingJackson2HttpMessageConverter
{
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private ObjectMapper a = new ObjectMapper();
    private String b;
    private Boolean c;

    public void setObjectMapper(ObjectMapper objectMapper)
    {
        Assert.notNull(objectMapper, "ObjectMapper must not be null");
        this.a = objectMapper;
        a();
    }

    private void a()
    {
        if (this.c != null) {
            this.a.configure(SerializationFeature.INDENT_OUTPUT, this.c.booleanValue());
        }
    }

    public ObjectMapper getObjectMapper()
    {
        return this.a;
    }

    public void setJsonPrefix(String jsonPrefix)
    {
        this.b = jsonPrefix;
    }

    public void setPrefixJson(boolean prefixJson)
    {
        this.b = (prefixJson ? "{} && " : null);
    }

    public void setPrettyPrint(boolean prettyPrint)
    {
        this.c = Boolean.valueOf(prettyPrint);
        a();
    }

    public boolean canRead(Class<?> clazz, MediaType mediaType)
    {
        return canRead(clazz, null, mediaType);
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType)
    {
        JavaType localJavaType = getJavaType(type, contextClass);
        return (this.a.canDeserialize(localJavaType)) && (canRead(mediaType));
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType)
    {
        return (this.a.canSerialize(clazz)) && (canWrite(mediaType));
    }

    protected boolean supports(Class<?> clazz)
    {
        throw new UnsupportedOperationException();
    }

    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        JavaType localJavaType = getJavaType(clazz, null);
        return a(localJavaType, inputMessage);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        JavaType localJavaType = getJavaType(type, contextClass);
        return a(localJavaType, inputMessage);
    }

    private Object a(JavaType paramJavaType, HttpInputMessage paramHttpInputMessage)
    {
        try
        {
            return this.a.readValue(paramHttpInputMessage.getBody(), paramJavaType);
        }
        catch (IOException localIOException)
        {
            throw new HttpMessageNotReadableException("Could not read JSON: " + localIOException.getMessage(), localIOException);
        }
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException
    {
        JsonEncoding localJsonEncoding = getJsonEncoding(outputMessage.getHeaders().getContentType());

        JsonGenerator localJsonGenerator = this.a.getJsonFactory().createJsonGenerator(outputMessage.getBody(), localJsonEncoding);
        if (this.a.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            localJsonGenerator.useDefaultPrettyPrinter();
        }
        try
        {
            if (this.b != null) {
                localJsonGenerator.writeRaw(this.b);
            }
            Map localMap;
            if (((object instanceof Map)) && (object != null))
            {
                if (((Map)object).containsKey("success"))
                {
                    this.a.writeValue(localJsonGenerator, object);
                }
                else
                {
                    localMap = ResultUtils.getSuccessResultData(object);
                    localMap.put("symbol", "RESULT_SYMBOL");
                    this.a.writeValue(localJsonGenerator, localMap);
                }
            }
            else
            {
                localMap = ResultUtils.getSuccessResultData(object);
                localMap.put("symbol", "RESULT_SYMBOL");
                this.a.writeValue(localJsonGenerator, localMap);
            }
        }
        catch (JsonProcessingException localJsonProcessingException1)
        {
            throw new HttpMessageNotWritableException("Could not write JSON: " + localJsonProcessingException1.getMessage(), localJsonProcessingException1);
        }
    }

    protected JavaType getJavaType(Type type, Class<?> contextClass)
    {
        return contextClass != null ? this.a.getTypeFactory().constructType(type, contextClass) : this.a.constructType(type);
    }

    protected JsonEncoding getJsonEncoding(MediaType contentType)
    {
        if ((contentType != null) && (contentType.getCharSet() != null))
        {
            Charset localCharset = contentType.getCharSet();
            for (JsonEncoding localJsonEncoding : JsonEncoding.values()) {
                if (localCharset.name().equals(localJsonEncoding.getJavaName())) {
                    return localJsonEncoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }
}
