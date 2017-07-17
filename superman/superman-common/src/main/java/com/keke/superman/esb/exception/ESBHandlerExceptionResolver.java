package com.keke.superman.esb.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keke.superman.esb.configuration.ServerConfiguration;
import com.keke.superman.esb.plugins.ServerPlugin;
import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.request.RequestHeader;
import com.keke.superman.esb.response.Response;
import com.keke.superman.esb.response.ResponseHeader;
import com.keke.superman.exception.BusinessRuntimeException;
import com.keke.superman.exception.ExceptionCode;
import com.keke.superman.exception.SystemRuntimeException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class ESBHandlerExceptionResolver
        extends AbstractHandlerExceptionResolver
        implements InitializingBean
{
    private static final Logger a = LoggerFactory.getLogger(ESBHandlerExceptionResolver.class);
    private ServerConfiguration b;
    private ExceptionConfiguration c;
    private static final ObjectMapper d = new ObjectMapper();
    @Value("#{applicationInfo['applicationCode']}")
    private String e = "00";

    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
    {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        Request localRequest = (Request)httpServletRequest.getAttribute("ESB_REQUEST");
        Response localResponse = a(localRequest, e);
        a.error("请求系统{}进行业务变为{}时处理异常，异常编号为:{}，异常信息为:{}", new Object[] { localRequest.getHeader().getReqSysCode(), localRequest.getHeader().getServiceCode(), Integer.valueOf(localResponse.getHeader().getReturnCode()), localResponse.getHeader().getReturnMessage() });
        a.error("ESB服务端处理失败", e);
        try
        {
            if ((this.b.getServerPlugins() != null) && (!this.b.getServerPlugins().isEmpty()))
            {
                List<ServerPlugin> localList = this.b.getServerPlugins();
                for (ServerPlugin localServerPlugin : localList) {
                    localServerPlugin.beforeResponse(localResponse);
                }
            }
            d.writeValue(httpServletResponse.getOutputStream(), localResponse);
        }
        catch (IOException localIOException1)
        {
            a.error("写入数据失败", localIOException1);
        }
        return null;
    }

    private Response a(Request paramRequest, Exception paramException)
    {
        Response localResponse = new Response();
        ResponseHeader localResponseHeader = new ResponseHeader();
        if (paramRequest != null)
        {
            RequestHeader localRequestHeader = paramRequest.getHeader();
            String str1 = localRequestHeader.getServiceCode();
            String str2 = localRequestHeader.getReqSysCode();
            localResponseHeader.setReqSysCode(str2);
            localResponseHeader.setServiceCode(str1);
            localResponseHeader.setProviderSysId(this.b.getProviderSysId());
        }
        a(paramException, localResponseHeader);
        localResponse.setHeader(localResponseHeader);
        return localResponse;
    }

    private void a(Exception paramException, ResponseHeader paramResponseHeader)
    {
        Map localMap = this.c.getESBExceptionInfo();
        ESBExceptionInfo localESBExceptionInfo = (ESBExceptionInfo)localMap.get(paramException.getClass().getName());
        if (localESBExceptionInfo != null)
        {
            paramResponseHeader.setReturnCode(localESBExceptionInfo.getCode());
        }
        else
        {
            int i = a(paramException);
            paramResponseHeader.setReturnCode(i);
        }
        if (this.b.isReturnExceptionStack()) {
            paramResponseHeader.setReturnMessage(ExceptionUtils.getStackTrace(paramException));
        } else if (StringUtils.isNoneEmpty(new CharSequence[] { paramException.getMessage() })) {
            paramResponseHeader.setReturnMessage(paramException.getMessage());
        } else if (localESBExceptionInfo != null) {
            paramResponseHeader.setReturnMessage(localESBExceptionInfo.getMessage());
        }
        if (ClassUtils.isAssignable(paramException.getClass(), BusinessRuntimeException.class)) {
            paramResponseHeader.setExceptionType(ExceptionType.BUSINESS_EXCEPTION);
        } else {
            paramResponseHeader.setExceptionType(ExceptionType.SYSTEM_EXCEPTION);
        }
        b(paramException, paramResponseHeader);
    }

    private int a(Exception paramException)
    {
        Class localClass = paramException.getClass();
        StringBuilder localStringBuilder = new StringBuilder("5");
        localStringBuilder.append(this.e);
        Object localObject;
        if (ClassUtils.isAssignable(localClass, BusinessRuntimeException.class))
        {
            localObject = (BusinessRuntimeException)paramException;
            if (((BusinessRuntimeException)localObject).getStateCode() != 0)
            {
                localStringBuilder.delete(0, localStringBuilder.length());
                localStringBuilder.append(((BusinessRuntimeException)localObject).getStateCode());
            }
            else
            {
                localStringBuilder.append("0000");
            }
        }
        else if (ClassUtils.isAssignable(localClass, SystemRuntimeException.class))
        {
            localObject = (SystemRuntimeException)paramException;
            if (((SystemRuntimeException)localObject).getStateCode() != 0)
            {
                localStringBuilder.delete(0, localStringBuilder.length());
                localStringBuilder.append(((SystemRuntimeException)localObject).getStateCode());
            }
            else
            {
                localStringBuilder.append("0000");
            }
        }
        else if (ClassUtils.isAssignable(localClass, MyBatisSystemException.class))
        {
            localStringBuilder.append(ExceptionCode.DATASOURCE_EXCEPTION_CODE.getCode());
        }
        else if (ClassUtils.isAssignable(localClass, IOException.class))
        {
            localStringBuilder.append(ExceptionCode.IO_EXCEPTION_CODE.getCode());
        }
        else
        {
            localStringBuilder.append(ExceptionCode.UNKNOWN_EXCEPTION_CODE.getCode());
        }
        return Integer.valueOf(localStringBuilder.toString()).intValue();
    }

    private void b(Exception paramException, ResponseHeader paramResponseHeader)
    {
        Field[] arrayOfField1 = FieldUtils.getAllFields(paramException.getClass());
        for (Field localField : arrayOfField1)
        {
            localField.setAccessible(true);
            Annotation localAnnotation = localField.getAnnotation(ReturnProperty.class);
            if (localAnnotation != null) {
                a(paramException, paramResponseHeader, localField);
            }
        }
    }

    private void a(Exception paramException, ResponseHeader paramResponseHeader, Field paramField)
    {
        String str = paramField.getName();
        try
        {
            Object localObject = paramField.get(paramException);
            if (localObject != null) {
                paramResponseHeader.getExceptionInfo().put(str, localObject);
            }
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
            a.error("无法获取异常对象中目标属性的值", localIllegalAccessException);
        }
    }

    public void setServerConfiguration(ServerConfiguration serverConfiguration)
    {
        this.b = serverConfiguration;
    }

    public void setExceptionConfiguration(ExceptionConfiguration exceptionConfiguration)
    {
        this.c = exceptionConfiguration;
    }

    public ServerConfiguration getServerConfiguration()
    {
        return this.b;
    }

    public void afterPropertiesSet()
            throws Exception
    {
        Assert.notNull(this.b, "请配置服务提供方配置信息");
        Assert.notNull(this.c, "请配置ESB服务端异常配置信息");
    }
}
