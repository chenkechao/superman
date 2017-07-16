package com.keke.superman.esb.exception;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class ESBRuntimeException
        extends RuntimeException
{
    private int a;
    private Map<String, Object> b = Maps.newConcurrentMap();
    private List<ESBRuntimeException> c;

    public ESBRuntimeException() {}

    public ESBRuntimeException(String message)
    {
        super(message);
    }

    public ESBRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ESBRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public List<ESBRuntimeException> getCauseBy()
    {
        return this.c;
    }

    public void setCauseBy(List<ESBRuntimeException> causeBy)
    {
        this.c = causeBy;
    }

    public int getStatuCode()
    {
        return this.a;
    }

    public void setStatuCode(int statuCode)
    {
        this.a = statuCode;
    }

    public Map<String, Object> getExceptionInfo()
    {
        return this.b;
    }

    public void setExceptionInfo(Map<String, Object> exceptionInfo)
    {
        this.b = exceptionInfo;
    }
}
