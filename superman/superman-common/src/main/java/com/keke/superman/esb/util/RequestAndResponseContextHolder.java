package com.keke.superman.esb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestAndResponseContextHolder
{
    public static final String RESPONSE_NAME_AT_ATTRIBUTES = ServletRequestAttributes.class
            .getName() + ".ATTRIBUTE_NAME";

    public static final HttpServletResponse response()
    {
        RequestAttributes localRequestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse localHttpServletResponse = null;
        if (localRequestAttributes != null)
        {
            ServletRequestAttributes localServletRequestAttributes = (ServletRequestAttributes)localRequestAttributes;
            localHttpServletResponse = (HttpServletResponse)localServletRequestAttributes.getAttribute(RESPONSE_NAME_AT_ATTRIBUTES, 0);
        }
        return localHttpServletResponse;
    }

    public static final HttpServletRequest request()
    {
        RequestAttributes localRequestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest localHttpServletRequest = null;
        if (localRequestAttributes != null)
        {
            ServletRequestAttributes localServletRequestAttributes = (ServletRequestAttributes)localRequestAttributes;
            localHttpServletRequest = localServletRequestAttributes.getRequest();
        }
        return localHttpServletRequest;
    }

    public static final SavedRequest savedRequest()
    {
        return (SavedRequest)request().getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
    }

    public static void setResponse(HttpServletResponse response)
    {
        RequestAttributes localRequestAttributes = RequestContextHolder.getRequestAttributes();
        if (localRequestAttributes != null)
        {
            ServletRequestAttributes localServletRequestAttributes = (ServletRequestAttributes)localRequestAttributes;
            localServletRequestAttributes.setAttribute(RESPONSE_NAME_AT_ATTRIBUTES, response, 0);
        }
    }
}
