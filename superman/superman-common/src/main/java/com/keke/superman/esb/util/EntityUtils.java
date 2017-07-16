package com.keke.superman.esb.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.keke.superman.esb.exception.ESBRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityUtils
{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger a = LoggerFactory.getLogger(EntityUtils.class);

    static
    {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setDateFormat(new ISO8601DateFormat());
    }

    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz)
    {
        try
        {
            String str = OBJECT_MAPPER.writeValueAsString(map);
            return (T)OBJECT_MAPPER.readValue(str, clazz);
        }
        catch (Exception localException1)
        {
            a.error("JSON 值转化失败，请检查。", localException1);
        }
        return null;
    }

    public static <T> T object2Bean(Object o, Class<T> clazz)
    {
        try
        {
            String str = OBJECT_MAPPER.writeValueAsString(o);
            return (T)OBJECT_MAPPER.readValue(str, clazz);
        }
        catch (IOException localIOException1)
        {
            a.error("实体转化失败", localIOException1);
            throw new ESBRuntimeException("实体转化失败", localIOException1);
        }
    }

    public static String toJSONString(Object object)
    {
        if (object != null) {
            try
            {
                return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            }
            catch (JsonProcessingException localJsonProcessingException)
            {
                a.error("JSON字符串转换出错", localJsonProcessingException);
                throw new ESBRuntimeException(localJsonProcessingException);
            }
        }
        return "";
    }

    public static byte[] toJSONByte(Object object)
    {
        if (object != null) {
            try
            {
                return OBJECT_MAPPER.writeValueAsBytes(object);
            }
            catch (JsonProcessingException localJsonProcessingException)
            {
                a.error("JSON字符串转换出错", localJsonProcessingException);
                throw new ESBRuntimeException(localJsonProcessingException);
            }
        }
        return new byte[0];
    }

    public static <T> List<T> list2Beans(List<Object> list, Class<?> clazz)
    {
        ArrayList localArrayList = Lists.newArrayList();
        try
        {
            for (Object localObject1 : list)
            {
                String str = OBJECT_MAPPER.writeValueAsString(localObject1);
                Object localObject2 = OBJECT_MAPPER.readValue(str, clazz);
                localArrayList.add(localObject2);
            }
        }
        catch (Exception localException1)
        {
            a.error("JSON 值转化失败，请检查", localException1);
        }
        return localArrayList;
    }

    public static Map<String, Object> map2Beans(Map<String, Object> map, Map<String, Class<?>> settings)
    {
        HashMap localHashMap = Maps.newHashMap();
        Iterator localIterator = settings.keySet().iterator();
        while (localIterator.hasNext())
        {
            String str = (String)localIterator.next();
            Object localObject1 = map.get(str);
            if (localObject1 == null)
            {
                localHashMap.put(str, null);
            }
            else
            {
                Class localClass = (Class)settings.get(str);
                Object localObject2;
                if ((localObject1 instanceof Map))
                {
                    localObject2 = map2Bean((Map)localObject1, localClass);
                    localHashMap.put(str, localObject2);
                }
                else if ((localObject1 instanceof List))
                {
                    localObject2 = list2Beans((List)localObject1, localClass);
                    localHashMap.put(str, localObject2);
                }
                else if (localObject1.getClass().equals(localClass))
                {
                    localHashMap.put(str, map.get(str));
                }
            }
        }
        return localHashMap;
    }

    public static <T> T readObjectFromJSONString(String str, Class<T> clazz)
    {
        return StringUtils.isEmpty(str) ? null : readObject(str.getBytes(), clazz);
    }

    public static <T> T readObject(byte[] data, Class<T> clazz)
    {
        JavaType localJavaType = OBJECT_MAPPER.constructType(clazz);
        try
        {
            return (T)OBJECT_MAPPER.readValue(data, localJavaType);
        }
        catch (IOException localIOException)
        {
            throw new ESBRuntimeException("无法读取json内容", localIOException);
        }
    }

    public static <T> T readObject(String data, Class<T> clazz)
    {
        JavaType localJavaType = OBJECT_MAPPER.constructType(clazz);
        try
        {
            return (T)OBJECT_MAPPER.readValue(data, localJavaType);
        }
        catch (IOException localIOException)
        {
            throw new ESBRuntimeException("无法读取json内容", localIOException);
        }
    }

    public static <T> T readObject(InputStream data, Class<T> clazz)
    {
        JavaType localJavaType = OBJECT_MAPPER.constructType(clazz);
        try
        {
            return (T)OBJECT_MAPPER.readValue(data, localJavaType);
        }
        catch (IOException localIOException)
        {
            throw new ESBRuntimeException("无法读取json内容", localIOException);
        }
    }
}
