package com.keke.superman.esb.exception;
import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keke.superman.esb.util.ESBCommonKit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class MultiHandlerExceptionResolver
        extends AbstractHandlerExceptionResolver
        implements InitializingBean
{
    private static final Logger a = LoggerFactory.getLogger(MultiHandlerExceptionResolver.class);
    private Map<AntPathRequestMatcher, AbstractHandlerExceptionResolver> b = Maps.newHashMap();
    private Map<AbstractHandlerExceptionResolver, List<String>> c = null;
    private AbstractHandlerExceptionResolver d = null;

    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
    {
        AbstractHandlerExceptionResolver localAbstractHandlerExceptionResolver = (AbstractHandlerExceptionResolver) ESBCommonKit.chooseObjectByPath(this.b, this.d);
        Object localObject = null;
        try
        {
            Method localMethod = localAbstractHandlerExceptionResolver.getClass().getMethod("resolveException", new Class[] { HttpServletRequest.class, HttpServletResponse.class, Object.class, Exception.class });
            localObject = localMethod.invoke(localAbstractHandlerExceptionResolver, new Object[] { httpServletRequest, httpServletResponse, o, e });
        }
        catch (Exception localException1)
        {
            a.error("类" + localAbstractHandlerExceptionResolver.getClass() + "方法doResolveException反射执行失败", localException1);
        }
        return localObject == null ? null : (ModelAndView)localObject;
    }

    public void afterPropertiesSet()
            throws Exception
    {
        Assert.notNull(this.c, "请配置处理异常的Resolver类");
        Set localSet = this.c.keySet();
        for (Iterator localIterator1 = localSet.iterator(); localIterator1.hasNext();)
        {
            AbstractHandlerExceptionResolver localAbstractHandlerExceptionResolver = (AbstractHandlerExceptionResolver)localIterator1.next();
            List<String> localList = (List)this.c.get(localAbstractHandlerExceptionResolver);
            for (String str : localList) {
                if ((StringUtils.equals("/", str)) || (StringUtils.equals("/**", str))) {
                    this.d = localAbstractHandlerExceptionResolver;
                } else {
                    this.b.put(new AntPathRequestMatcher(str), localAbstractHandlerExceptionResolver);
                }
            }
        }
        AbstractHandlerExceptionResolver localAbstractHandlerExceptionResolver;
    }

    public void setResolverMap(Map<AbstractHandlerExceptionResolver, List<String>> resolverMap)
    {
        this.c = resolverMap;
    }
}
