package com.keke.superman.util;

import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class ContextHolder implements InitializingBean, ApplicationContextAware, ServletContextAware
{
    private static Logger a = LoggerFactory.getLogger(ContextHolder.class);
    public static ApplicationContext appContext;
    public static ServletContext servletContext;
    public static String WEB_ROOT_DIR_PATH;
    public static File STATIC_ROOT_DIR;

    public static <T> T getBean(String beanName)
    {
        if (appContext == null)
        {
            a.error("applicationContext����������");
            return null;
        }
        if (StringUtils.isEmpty(beanName))
        {
            a.error("bean��������������");
            return null;
        }
        Object localObject = null;
        try
        {
            localObject = appContext.getBean(beanName);
        }
        catch (Exception localException)
        {
            a.error("����spring bean[" + beanName + "]������" + localException.getMessage(), localException);
            return null;
        }
        if (localObject == null)
        {
            a.error("������ID��[" + beanName + "]��spring bean������������spring������");
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
            return (Optional<T>) Optional.of(localObject);//TODO
        }
        catch (BeansException localBeansException1)
        {
            a.error("����spring bean[" + beanName + "]����", localBeansException1);
        }
        return Optional.absent();
    }

    public void setApplicationContext(ApplicationContext context)
            throws BeansException
    {
        if (appContext != null) {
            throw new BeanCreationException("applicationContext������������");
        }
        appContext = context;
        a.info("holded applicationContext,displayName:" + appContext.getDisplayName());
    }

    public void setServletContext(ServletContext context)
    {
        servletContext = context;
    }

    public void afterPropertiesSet()
            throws Exception
    {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.MULTI_LINE_STYLE);
        if (servletContext != null)
        {
            WEB_ROOT_DIR_PATH = servletContext.getRealPath("");

            STATIC_ROOT_DIR = new File(resolveEmbeddedValue("${static.root.path}"));
        }
    }

    public static final String resolveEmbeddedValue(String propName)
    {
        return ((AbstractRefreshableApplicationContext)appContext).getBeanFactory().resolveEmbeddedValue(propName);
    }
}

