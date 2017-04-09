package com.keke.superman.persist.service;

import com.google.common.base.Optional;
import com.keke.superman.exception.CannotResolverMapperByGenericTypeException;
import com.keke.superman.persist.mapper.BaseMapper;
import com.keke.superman.persist.model.BaseExample;
import com.keke.superman.persist.model.BaseModel;
import com.keke.superman.util.ContextHolder;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public abstract class AbstractService<PK extends Serializable, Model extends BaseModel, Example extends BaseExample, Mapper extends BaseMapper>
        implements IService<PK, Model, Example, Mapper>
{
    public final Logger a = LoggerFactory.getLogger(getClass());
    protected BaseMapper<PK, Model, Example> b;

    protected BaseMapper<PK, Model, Example> a()
    {
        if (this.b == null) {
            this.b = ((BaseMapper)b().get());
        }
        return this.b;
    }

    //TODO
    //private Optional<BaseMapper<PK, Model, Example>> b()
    private Optional<BaseMapper> b()
    {
        Type localType = getClass().getGenericSuperclass();
        if (TypeUtils.isInstance(localType, ParameterizedType.class))
        {
            ParameterizedType localParameterizedType = (ParameterizedType)localType;
            Type[] arrayOfType = localParameterizedType.getActualTypeArguments();
            Class localClass = (Class)arrayOfType[3];
            BaseMapper localBaseMapper = (BaseMapper) ContextHolder.getBean(localClass);
            return Optional.of(localBaseMapper);
        }
        throw new CannotResolverMapperByGenericTypeException();
    }
}

