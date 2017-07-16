package com.keke.superman.esb.request;

import java.io.Serializable;

public class Request<T>
        implements Serializable
{
    private static final long a = -3166337815368214963L;
    private RequestHeader b;
    private T c;

    public RequestHeader getHeader()
    {
        return this.b;
    }

    public void setHeader(RequestHeader header)
    {
        this.b = header;
    }

    public T getBody()
    {
        return (T)this.c;
    }

    public void setBody(T body)
    {
        this.c = body;
    }
}
