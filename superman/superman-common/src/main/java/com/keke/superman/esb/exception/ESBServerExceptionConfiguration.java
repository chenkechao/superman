package com.keke.superman.esb.exception;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

import com.keke.superman.esb.client.ESBClient;
import com.keke.superman.esb.response.Response;
import com.keke.superman.esb.util.ESBContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ESBServerExceptionConfiguration
        implements ExceptionConfiguration, ApplicationListener<ContextRefreshedEvent>
{
    private Map<String, ESBExceptionInfo> a = Maps.newConcurrentMap();
    private String b;
    private String c = "esbClient";
    private static final Logger d = LoggerFactory.getLogger(ESBServerExceptionConfiguration.class);

    public Map<String, ESBExceptionInfo> getESBExceptionInfo()
    {
        return this.a;
    }

    public String getExceptionServiceCode()
    {
        return this.b;
    }

    public void setExceptionServiceCode(String exceptionServiceCode)
    {
        this.b = exceptionServiceCode;
    }

    public Map<String, ESBExceptionInfo> getEsbExceptionInfos()
    {
        return this.a;
    }

    public void setEsbExceptionInfos(Map<String, ESBExceptionInfo> esbExceptionInfos)
    {
        this.a = esbExceptionInfos;
    }

    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if (event.getApplicationContext().getParent() != null)
        {
            ESBClient localESBClient = (ESBClient) ESBContextHolder.getBean(this.c);
            Response localResponse = localESBClient.request(this.b, null);
            if (localResponse == null) {
                d.error("ESB异常配置信息加载失败，请检查ESB引擎是否正常启动");
            } else {
                a(localResponse);
            }
        }
    }

    private void a(Response paramResponse)
    {
        List<Map> localList = (List)paramResponse.getBody();
        if (localList != null) {
            for (Map localMap : localList)
            {
                ESBExceptionInfo localESBExceptionInfo = new ESBExceptionInfo();
                localESBExceptionInfo.setCode(Integer.valueOf(localMap.get("code").toString()).intValue());
                localESBExceptionInfo.setExceptionClass(localMap.get("exceptionClass").toString());
                localESBExceptionInfo.setMessage(localMap.get("message").toString());
                this.a.put(localESBExceptionInfo.getExceptionClass(), localESBExceptionInfo);
            }
        }
    }

    public String getEsbClientBeanName()
    {
        return this.c;
    }

    public void setEsbClientBeanName(String esbClientBeanName)
    {
        this.c = esbClientBeanName;
    }
}
