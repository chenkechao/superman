package com.keke.superman.esb.converter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.keke.superman.esb.util.ESBCommonKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

public class JSONMessageConverter
        implements InitializingBean, HttpMessageConverter<Object>
{
    private Map<HttpMessageConverter, List<String>> a;
    private Map<AntPathRequestMatcher, HttpMessageConverter> b = Maps.newHashMap();
    private HttpMessageConverter c;
    private List<MediaType> d = Lists.newArrayList();

    public boolean canRead(Class<?> clazz, MediaType mediaType)
    {
        HttpMessageConverter localHttpMessageConverter = (HttpMessageConverter) ESBCommonKit.chooseObjectByPath(this.b, this.c);
        return localHttpMessageConverter.canRead(clazz, mediaType);
    }

    public boolean canWrite(Class<?> clazz, MediaType mediaType)
    {
        HttpMessageConverter localHttpMessageConverter = (HttpMessageConverter)ESBCommonKit.chooseObjectByPath(this.b, this.c);
        return localHttpMessageConverter.canWrite(clazz, mediaType);
    }

    public List<MediaType> getSupportedMediaTypes()
    {
        return this.d;
    }

    public Object read(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException
    {
        HttpMessageConverter localHttpMessageConverter = (HttpMessageConverter)ESBCommonKit.chooseObjectByPath(this.b, this.c);
        return localHttpMessageConverter.read(clazz, inputMessage);
    }

    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException
    {
        HttpMessageConverter localHttpMessageConverter = (HttpMessageConverter)ESBCommonKit.chooseObjectByPath(this.b, this.c);
        localHttpMessageConverter.write(o, contentType, outputMessage);
    }

    public Map<HttpMessageConverter, List<String>> getConverterPathMap()
    {
        return this.a;
    }

    public void setConverterPathMap(Map<HttpMessageConverter, List<String>> converterPathMap)
    {
        this.a = converterPathMap;
    }

    public void afterPropertiesSet()
            throws Exception
    {
        Assert.notNull(this.a, "请配置转换器信息");
        Set<HttpMessageConverter> localSet = this.a.keySet();
        for (HttpMessageConverter localHttpMessageConverter : localSet)
        {
            List<String> localList = (List)this.a.get(localHttpMessageConverter);
            for (String str : localList) {
                if ((StringUtils.equals("/", str)) || (StringUtils.equals("/**", str))) {
                    this.c = localHttpMessageConverter;
                } else {
                    this.b.put(new AntPathRequestMatcher(str), localHttpMessageConverter);
                }
            }
            if (localHttpMessageConverter.getSupportedMediaTypes() != null) {
                this.d.addAll(localHttpMessageConverter.getSupportedMediaTypes());
            }
        }
        Assert.notNull(this.c, "必须配置默认的消息转换器，路径配置为/时，该消息转换类为默认的转换类");
    }
}
