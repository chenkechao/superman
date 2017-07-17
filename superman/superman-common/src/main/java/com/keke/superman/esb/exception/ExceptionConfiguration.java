package com.keke.superman.esb.exception;


import java.util.Map;

public abstract interface ExceptionConfiguration
{
    public abstract Map<String, ESBExceptionInfo> getESBExceptionInfo();
}
