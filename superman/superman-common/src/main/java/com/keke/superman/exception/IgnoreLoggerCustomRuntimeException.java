package com.keke.superman.exception;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;

public class IgnoreLoggerCustomRuntimeException
        extends IllegalArgumentException
{
    private List<String> errorMessages;

    public IgnoreLoggerCustomRuntimeException() {}

    public IgnoreLoggerCustomRuntimeException(String message)
    {
        super(message);
        this.errorMessages = Arrays.asList(new String[] { message });
    }

    public IgnoreLoggerCustomRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
        this.errorMessages = Arrays.asList(new String[] { message });
    }

    public IgnoreLoggerCustomRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public IgnoreLoggerCustomRuntimeException(String message, String[] reset)
    {
        super(message);
        this.errorMessages = Lists.asList(message, reset);
    }

    public List<String> getErrorMessages()
    {
        return this.errorMessages;
    }
}

