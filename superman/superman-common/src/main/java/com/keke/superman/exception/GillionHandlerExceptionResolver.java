package com.keke.superman.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keke.superman.esb.util.JsonMapperHolder;
import com.keke.superman.esb.util.ResponseUtils;
import com.keke.superman.esb.util.ResultUtils;
import com.keke.superman.security.data.exception.DataPermissionException;
import com.keke.superman.util.ContextHolder;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

public class GillionHandlerExceptionResolver
        extends AbstractHandlerMethodExceptionResolver
        implements InitializingBean
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(GillionHandlerExceptionResolver.class);
    private String exViewName;
    @Value("#{applicationInfo['applicationCode']}")
    private String applicationCode;

    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex)
    {
        response.setStatus(500);
        Class localClass = ex.getClass();
        if (!ClassUtils.isAssignable(localClass, IgnoreLoggerCustomRuntimeException.class)) {
            this.logger.error(ex.getMessage(), ex);
        }
        StringBuilder localStringBuilder = new StringBuilder("5");
        localStringBuilder.append(this.applicationCode);
        String str;
        if (ClassUtils.isAssignable(localClass, IgnoreLoggerCustomRuntimeException.class))
        {
            localStringBuilder.append(ExceptionCode.UNKNOWN_EXCEPTION_CODE.getCode());
            str = StringUtils.join(((IgnoreLoggerCustomRuntimeException)ex).getErrorMessages(), "<br/>");
        }
        else if (ClassUtils.isAssignable(localClass, BusinessRuntimeException.class))
        {
            BusinessRuntimeException localObject1 = (BusinessRuntimeException)ex;
            localStringBuilder.delete(0, localStringBuilder.length());
            localStringBuilder.append(((BusinessRuntimeException)localObject1).getStateCode());
            str = ex.getMessage();
        }
        else if (ClassUtils.isAssignable(localClass, SystemRuntimeException.class))
        {
            SystemRuntimeException localObject1 = (SystemRuntimeException)ex;
            localStringBuilder.delete(0, localStringBuilder.length());
            localStringBuilder.append(((SystemRuntimeException)localObject1).getStateCode());
            str = "系统异常，请联系IT管理员";
        }
        else if (ClassUtils.isAssignable(localClass, MyBatisSystemException.class))
        {
            Throwable localObject1 = Exceptions.getRootCause(ex);
            if ((localObject1 instanceof DataPermissionException)) {
                str = ((Throwable)localObject1).getMessage();
            } else {
                str = "数据库操作异常，请联系应用开发者处理！";
            }
            localStringBuilder.append(ExceptionCode.DATASOURCE_EXCEPTION_CODE.getCode());
        }
        else if (ClassUtils.isAssignable(localClass, IOException.class))
        {
            str = "IO异常，请联系开发者处理！";
            localStringBuilder.append(ExceptionCode.IO_EXCEPTION_CODE.getCode());
        }
        else
        {
            if (StringUtils.isNotBlank(ex.getMessage())) {
                str = ex.getMessage();
            } else {
                str = "未知异常，请联系开发者处理！";
            }
            localStringBuilder.append(ExceptionCode.UNKNOWN_EXCEPTION_CODE.getCode());
        }
        Object localObject1 = Integer.valueOf(localStringBuilder.toString());
        Object localObject2;
        if (isAjaxRequest(request))
        {
            localObject2 = ResultUtils.getFaildResultDataWithErrorCode(localObject1, new String[] { str });
            ResponseUtils.renderJson(response, JsonMapperHolder.jsonMapper.toJson(localObject2));
        }
        else
        {
            if (StringUtils.isNotBlank(this.exViewName))
            {
                localObject2 = new ModelAndView();
                ((ModelAndView)localObject2).addObject("errorMessage", str);
                return (ModelAndView)localObject2;
            }
            ResponseUtils.render(response, "text/html; charset=utf-8", "<html><body><h2 style=\"color:red;font:bold;\">" + str + "</h2></body></html>");
        }
        return null;
    }

    private boolean isAjaxRequest(HttpServletRequest request)
    {
        return StringUtils.isNotBlank(request.getHeader("X-Requested-With"));
    }

    public void afterPropertiesSet()
            throws Exception
    {
        try
        {
            this.exViewName = ContextHolder.resolveEmbeddedValue("${mvc.exceptionResolver.viewName}");
        }
        catch (Exception localException) {}
    }
}
