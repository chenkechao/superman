package com.keke.superman.exception;

public abstract class CustomRuntimeException
        extends RuntimeException
{
    public CustomRuntimeException()
    {
        super("自定义运行异常，请联系系统开发者处理！");
    }

    public CustomRuntimeException(String message)
    {
        super(message);
    }

    public CustomRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CustomRuntimeException(Throwable cause)
    {
        super(cause);
    }
}
