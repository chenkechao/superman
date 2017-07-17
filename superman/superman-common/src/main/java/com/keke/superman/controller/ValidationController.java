package com.keke.superman.controller;

import com.google.common.base.CaseFormat;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import com.keke.superman.esb.util.ResultUtils;
import com.keke.superman.util.ContextHolder;
import com.keke.superman.validation.model.UniqueVo;
import com.keke.superman.validation.service.UniqueService;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.utils.Reflections;

@Controller
public class ValidationController
        implements InitializingBean, ApplicationContextAware
{
    private File a;
    @Value("${validation.path}")
    private String b;
    @Value("${i18n.nls.path}")
    private String c;
    private ApplicationContext d;
    @Autowired
    private UniqueService e;

    @ResponseBody
    @RequestMapping(value={"/validation/unique"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public Map<String, Object> unique(@RequestBody UniqueVo uniqueVo)
    {
        if (this.e.validUnique(uniqueVo)) {
            return ResultUtils.getSuccessResultData();
        }
        return ResultUtils.getFaildResultData();
    }

    @ResponseBody
    @RequestMapping({"/validation/exists/{entityName}/{fieldName}/{value}"})
    public Map<String, Object> dynamicExists(@PathVariable String entityName, @PathVariable String fieldName, @PathVariable String value)
            throws IllegalArgumentException
    {
        String str1 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, entityName).concat("Service");
        boolean bool = this.d.containsBean(str1);

        Validate.isTrue(bool, "检验唯一性的实体Service不存在，可能是注入, serviceBeanName: %s", new Object[] { str1 });
        Object localObject1 = this.d.getBean(str1);

        String str2 = "exists" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fieldName);
        Method localMethod = Reflections.getAccessibleMethodByName(localObject1, str2);

        Validate.notNull(localMethod, "检验唯一性的实体Service不存在校验对应字段的方法，可能是注入, ser viceBeanName: %s, methodName: %s", new Object[] { str1, str2 });
        Class[] arrayOfClass = localMethod.getParameterTypes();

        Validate.notEmpty(arrayOfClass, "检验方法必须有唯一参数", new Object[0]);
        Validate.isTrue(arrayOfClass.length == 1, "检验方法必须有唯一参数", new Object[0]);
        Class localClass = arrayOfClass[0];
        try
        {
            Object localObject2 = MethodUtils.invokeStaticMethod(localClass, "valueOf", new Object[] { value });
            Boolean localBoolean = (Boolean)localMethod.invoke(localObject1, new Object[] { localObject2 });
            if (localBoolean.booleanValue()) {
                return ResultUtils.getFaildResultData();
            }
            return ResultUtils.getSuccessResultData();
        }
        catch (Exception localException1)
        {
            throw new IllegalArgumentException("验证的值只支持基础类型，或有valueOf(String)方法的类型.");
        }
    }

    public void afterPropertiesSet()
            throws Exception
    {
        this.a = new File(ContextHolder.STATIC_ROOT_DIR, this.b);
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        this.d = applicationContext;
    }
}