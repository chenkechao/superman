package com.keke.superman.exception;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class CannotResolverMapperByGenericTypeException
        extends RuntimeException
{
    private static final long a = -2252307191778265676L;

    public CannotResolverMapperByGenericTypeException()
    {
        super("不能通过泛型信息获取业务类对应的'Mybatis Mapper'实现");
    }

    public CannotResolverMapperByGenericTypeException(String message)
    {
        super(message);
    }

    public CannotResolverMapperByGenericTypeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CannotResolverMapperByGenericTypeException(Throwable cause)
    {
        super(cause);
    }
}