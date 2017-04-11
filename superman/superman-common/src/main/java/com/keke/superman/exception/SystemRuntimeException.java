package com.keke.superman.exception;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class SystemRuntimeException
        extends NestableRuntimeException
{
    private static final long serialVersionUID = 3976739176979050800L;
    private int stateCode = 5000000;

    public SystemRuntimeException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public SystemRuntimeException(String msg)
    {
        super(msg);
    }

    public SystemRuntimeException(int stateCode)
    {
        this.stateCode = stateCode;
    }

    public SystemRuntimeException(int stateCode, String msg)
    {
        super(msg);
        this.stateCode = stateCode;
    }

    public SystemRuntimeException(int stateCode, Throwable cause)
    {
        super(cause);
        this.stateCode = stateCode;
    }

    public SystemRuntimeException(int stateCode, String msg, Throwable cause)
    {
        super(msg, cause);
        this.stateCode = stateCode;
    }

    public int getStateCode()
    {
        return this.stateCode;
    }

    public void setStateCode(int stateCode)
    {
        this.stateCode = stateCode;
    }
}

