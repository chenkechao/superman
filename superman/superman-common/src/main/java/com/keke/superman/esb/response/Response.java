package com.keke.superman.esb.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.keke.superman.esb.util.EntityUtils;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(isGetterVisibility=JsonAutoDetect.Visibility.NONE)
public class Response
        implements Serializable
{
    private static final long a = -6379822256603277872L;
    private ResponseHeader b;
    private Object c;

    @JsonIgnore
    public int getStatusCode()
    {
        if (this.b == null) {
            return 5000000;
        }
        return this.b.getReturnCode();
    }

    @JsonIgnore
    public <R> R getBody(Class t)
    {
        if (this.c != null)
        {
            Object localObject2 = getBody();
            if ((localObject2 instanceof List))
            {
                List localList = EntityUtils.list2Beans((List)localObject2, t);
                return (R)localList;
            }
            Object localObject1 = EntityUtils.object2Bean(this.c, t);
            return (R)localObject1;
        }
        return null;
    }

    public Object getBody()
    {
        return this.c;
    }

    public <T> void parseBody(Class<T> clazz)
    {
        if (this.c != null)
        {
            Object localObject = EntityUtils.object2Bean(this.c, clazz);
            this.c = localObject;
        }
    }

    public ResponseHeader getHeader()
    {
        return this.b;
    }

    public void setHeader(ResponseHeader header)
    {
        this.b = header;
    }

    public void setBody(Object body)
    {
        this.c = body;
    }

    public String toString()
    {
        return EntityUtils.toJSONString(this);
    }
}
