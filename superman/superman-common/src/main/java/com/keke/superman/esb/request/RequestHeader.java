package com.keke.superman.esb.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Properties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestHeader
        implements Serializable
{
    private static final long a = -5106017621472005967L;
    private String b;
    private String c;
    private String d;
    private Properties e = new Properties();

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

    public Properties getProperties()
    {
        return this.e;
    }

    public void setProperties(Properties properties)
    {
        this.e = properties;
    }

    public String getSerialNumber()
    {
        return this.d;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.d = serialNumber;
    }

    public String toString()
    {
        return "RequestHeader{serviceCode='" + this.b + '\'' + ", reqSysCode='" + this.c + '\'' + ", serialNumber='" + this.d + '\'' + ", properties=" + this.e + '}';
    }
}