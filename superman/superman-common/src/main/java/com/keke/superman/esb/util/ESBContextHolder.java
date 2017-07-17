package com.keke.superman.esb.util;


import com.google.common.base.Optional;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class ESBContextHolder
        implements ApplicationContextAware, ServletContextAware
{
    private static Logger a = LoggerFactory.getLogger(ESBContextHolder.class);
    public static ApplicationContext appContext;
    public static ServletContext servletContext;

    public static <T> T getBean(String beanName)
    {
        if (appContext == null)
        {
            a.error("applicationContext不能为空");
            return null;
        }
        if (StringUtils.isEmpty(beanName))
        {
            a.error("bean名称不能为空");
            return null;
        }
        Object localObject = null;
        try
        {
            localObject = appContext.getBean(beanName);
        }
        catch (Exception localException)
        {
            a.error("获取spring bean[" + beanName + "]错误" + localException.getMessage(), localException);
            return null;
        }
        if (localObject == null)
        {
            a.error("找不到ID为[" + beanName + "]的spring bean实现，请检查spring配置");
            return null;
        }
        return (T)localObject;
    }

    public static final <T> T getBean(Class<T> requireType)
    {
        return (T)appContext.getBean(requireType);
    }

    public static final <T> Optional<T> getOptionalBean(String beanName)
    {
        try
        {
            Object localObject = appContext.getBean(beanName);
            return Optional.of((T) localObject);
        }
        catch (BeansException localBeansException1)
        {
            a.error("获取spring bean[" + beanName + "]出错", localBeansException1);
        }
        return Optional.absent();
    }

    public void setApplicationContext(ApplicationContext context)
    {
        if (appContext != null) {
            throw new BeanCreationException("applicationContext已经被初始化");
        }
        appContext = context;
    }

    public void setServletContext(ServletContext context)
    {
        servletContext = context;
    }
}