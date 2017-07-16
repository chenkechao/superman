package com.keke.superman.esb.converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keke.superman.esb.configuration.ServerConfiguration;
import com.keke.superman.esb.plugins.ServerPlugin;
import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.request.RequestHeader;
import com.keke.superman.esb.response.Response;
import com.keke.superman.esb.response.ResponseHeader;
import com.keke.superman.esb.util.ESBCommonKit;
import com.keke.superman.esb.util.EntityUtils;
import com.keke.superman.esb.util.RequestAndResponseContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ESBMessageConverter
        extends MappingJackson2HttpMessageConverter
        implements InitializingBean
{
    public static final Logger ESB_LOGGER = LoggerFactory.getLogger(ESBMessageConverter.class);
    private ObjectMapper a = EntityUtils.OBJECT_MAPPER;
    private String b;
    private Boolean c;
    private ServerConfiguration d;

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
            throws IOException
    {
        JavaType localJavaType = getJavaType(clazz, null);
        return a(localJavaType, inputMessage);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException
    {
        JavaType localJavaType = getJavaType(type, contextClass);
        return a(localJavaType, inputMessage);
    }

    private Object a(JavaType paramJavaType, HttpInputMessage paramHttpInputMessage)
    {
        try
        {
            Request localRequest = (Request)this.a.readValue(paramHttpInputMessage.getBody(), Request.class);
            if (localRequest.getHeader() == null) {
                localRequest.setHeader(new RequestHeader());
            }
            if (ESB_LOGGER.isDebugEnabled()) {
                ESB_LOGGER.debug("ESB请求报文为：\n{}", this.a.writerWithDefaultPrettyPrinter().writeValueAsString(localRequest));
            }
            RequestAttributes localRequestAttributes = RequestContextHolder.getRequestAttributes();
            if (localRequestAttributes != null)
            {
                ServletRequestAttributes localServletRequestAttributes = (ServletRequestAttributes)localRequestAttributes;
                HttpServletRequest localHttpServletRequest = localServletRequestAttributes.getRequest();
                localHttpServletRequest.setAttribute("ESB_REQUEST", localRequest);
            }
            return a(paramJavaType, localRequest);
        }
        catch (IOException localIOException)
        {
            throw new HttpMessageNotReadableException("Could not read JSON: " + localIOException.getMessage(), localIOException);
        }
    }

    private Object a(JavaType paramJavaType, Request<Object> paramRequest)
            throws IOException
    {
        Object localObject1 = paramRequest.getBody();
        Object localObject2;
        if ((localObject1 instanceof List))
        {
            if (paramJavaType.isArrayType())
            {
                localObject2 = EntityUtils.toJSONString(localObject1);
                return this.a.readValue((String)localObject2, paramJavaType);
            }
        }
        else if ((localObject1 instanceof Map)) {
            localObject1 = EntityUtils.map2Bean((Map)paramRequest.getBody(), paramJavaType.getRawClass());
        }
        paramRequest.setBody(localObject1);
        if ((this.d.getServerPlugins() != null) && (!this.d.getServerPlugins().isEmpty()))
        {
            localObject2 = this.d.getServerPlugins();
            for (ServerPlugin localServerPlugin : (List<ServerPlugin>)localObject2) {
                localServerPlugin.afterRequest(paramRequest);
            }
        }
        return localObject1;
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException
    {
        HttpServletResponse localHttpServletResponse = RequestAndResponseContextHolder.response();
        if (localHttpServletResponse != null) {
            localHttpServletResponse.setContentType("application/json");
        }
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
            Response localResponse = a(object);
            if ((this.d.getServerPlugins() != null) && (!this.d.getServerPlugins().isEmpty()))
            {
                List<ServerPlugin> localList = this.d.getServerPlugins();
                for (ServerPlugin localServerPlugin : localList) {
                    localServerPlugin.beforeResponse(localResponse);
                }
            }
            if (ESB_LOGGER.isDebugEnabled()) {
                ESB_LOGGER.debug("ESB请求报文为：\n{}", EntityUtils.toJSONString(localResponse));
            }
            this.a.writeValue(localJsonGenerator, localResponse);
        }
        catch (JsonProcessingException localJsonProcessingException1)
        {
            throw new HttpMessageNotWritableException("Could not write JSON: " + localJsonProcessingException1.getMessage(), localJsonProcessingException1);
        }
    }

    private Response a(Object paramObject)
    {
        HttpServletRequest localHttpServletRequest = ESBCommonKit.request();
        Request localRequest = (Request)localHttpServletRequest.getAttribute("ESB_REQUEST");
        RequestHeader localRequestHeader = localRequest.getHeader();
        Response localResponse = new Response();
        ResponseHeader localResponseHeader = new ResponseHeader();
        localResponseHeader.setReqSysCode(localRequestHeader.getReqSysCode());
        localResponseHeader.setServiceCode(localRequestHeader.getServiceCode());
        localResponseHeader.setProviderSysId(this.d.getProviderSysId());
        localResponseHeader.setReturnCode(2000000);
        localResponseHeader.setReturnMessage(null);
        localResponseHeader.setSerialNumber(localRequestHeader.getSerialNumber());
        localResponse.setHeader(localResponseHeader);
        localResponse.setBody(paramObject);
        return localResponse;
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

    public ServerConfiguration getServerConfiguration()
    {
        return this.d;
    }

    public void setServerConfiguration(ServerConfiguration serverConfiguration)
    {
        this.d = serverConfiguration;
    }

    public void afterPropertiesSet()
            throws Exception
    {
        Assert.notNull(this.d, "请配置ESB接受服务器端配置信息");
    }
}
