package com.keke.superman.esb.exception;

import org.apache.commons.lang3.StringUtils;

public class ESBExceptionInfo
{
    private int a;
    private String b;
    private String c;

    public void setCode(int code)
    {
        this.a = code;
    }

    public int getCode()
    {
        return this.a;
    }

    public String getMessage()
    {
        return this.c;
    }

    public void setMessage(String message)
    {
        this.c = message;
    }

    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        ESBExceptionInfo localESBExceptionInfo = (ESBExceptionInfo)o;
        if (this.b != null ? !this.b.equals(localESBExceptionInfo.b) : localESBExceptionInfo.b != null) {
            return false;
        }
        return true;
    }

    public String getExceptionClass()
    {
        return this.b;
    }

    public void setExceptionClass(String exceptionClass)
    {
        this.b = exceptionClass;
    }

    public int hashCode()
    {
        return this.b != null ? this.b.hashCode() : 0;
    }

    public boolean match(Exception e)
    {
        return StringUtils.equals(e.getClass().getName(), this.b);
    }
}