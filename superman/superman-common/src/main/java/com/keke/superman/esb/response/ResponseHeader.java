package com.keke.superman.esb.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;
import com.keke.superman.esb.exception.ExceptionType;
import com.keke.superman.esb.util.EntityUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponseHeader
        implements Serializable
{
    public static final int DEFAULT_SUCCESS_CODE = 2000000;
    public static final int DEFAULT_FAILURE_CODE = 5000000;
    public static final String COMMNAD_BATCH = "command/batch";
    public static final String COMMNAD_SIMPLE = "command/simple";
    private static final long a = 761952646612512462L;
    private String b;
    private String c;
    private String d;
    private int e;
    private String f;
    private ExceptionType g;
    private String h;
    private String i;
    private Properties j = new Properties();
    private Map<String, Object> k = Maps.newConcurrentMap();

    public String getServiceCode()
    {
        return this.b;
    }

    public void setServiceCode(String serviceCode)
    {
        this.b = serviceCode;
    }

    public String getReqSysCode()
    {
        return this.c;
    }

    public void setReqSysCode(String reqSysCode)
    {
        this.c = reqSysCode;
    }

    public String getProviderSysId()
    {
        return this.d;
    }

    public void setProviderSysId(String providerSysId)
    {
        this.d = providerSysId;
    }

    public int getReturnCode()
    {
        return this.e;
    }

    public void setReturnCode(int returnCode)
    {
        this.e = returnCode;
    }

    public String getReturnMessage()
    {
        return this.f;
    }

    public void setReturnMessage(String returnMessage)
    {
        this.f = returnMessage;
    }

    public String getContentType()
    {
        return this.h;
    }

    public void setContentType(String contentType)
    {
        this.h = contentType;
    }

    public Map<String, Object> getExceptionInfo()
    {
        return this.k;
    }

    public void setExceptionInfo(Map<String, Object> exceptionInfo)
    {
        this.k = exceptionInfo;
    }

    public Properties getProperties()
    {
        return this.j;
    }

    public void setProperties(Properties properties)
    {
        this.j = properties;
    }

    public String getSerialNumber()
    {
        return this.i;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.i = serialNumber;
    }

    public ExceptionType getExceptionType()
    {
        return this.g;
    }

    public void setExceptionType(ExceptionType exceptionType)
    {
        this.g = exceptionType;
    }

    public String toString()
    {
        return EntityUtils.toJSONString(this);
    }
}
