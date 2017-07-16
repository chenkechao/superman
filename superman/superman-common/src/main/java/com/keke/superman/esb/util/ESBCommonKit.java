package com.keke.superman.esb.util;

import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ESBCommonKit
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

    public static <T> T chooseObjectByPath(Map<AntPathRequestMatcher, T> objectMap)
    {
        Object localObject = null;
        Set<AntPathRequestMatcher> localSet = objectMap.keySet();
        for (AntPathRequestMatcher localAntPathRequestMatcher : localSet) {
            if (localAntPathRequestMatcher.matches(RequestAndResponseContextHolder.request())) {
                localObject = objectMap.get(localAntPathRequestMatcher);
            }
        }
        return (T)localObject;
    }

    public static <T> T chooseObjectByPath(Map<AntPathRequestMatcher, T> objectMap, T defaultObject)
    {
        Object localObject = chooseObjectByPath(objectMap);
        if (localObject == null) {
            localObject = defaultObject;
        }
        return (T)localObject;
    }
}

