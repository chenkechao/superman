package com.keke.superman.esb.client;

import com.keke.superman.esb.exception.ESBRuntimeException;
import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.response.Response;

import java.util.List;

public abstract interface ESBClient
{
    public abstract Response request(Request paramRequest);

    public abstract Response request(String paramString, Object paramObject);

    public abstract Response request(String paramString, Object paramObject, Class paramClass);

    public abstract <R> R invoke(String paramString, Object paramObject, Class paramClass)
            throws ESBRuntimeException;

    @Deprecated
    public abstract Response invokeBatch(String paramString, Object paramObject, Class paramClass)
            throws ESBRuntimeException;

    public abstract <T> List<T> invokeBatch(String paramString, Object paramObject, Class paramClass, List<ESBRuntimeException> paramList)
            throws ESBRuntimeException;
}
