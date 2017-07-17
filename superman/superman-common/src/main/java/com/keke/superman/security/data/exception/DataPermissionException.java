package com.keke.superman.security.data.exception;

public class DataPermissionException
        extends Exception
{
    private static final long a = 1L;

    public DataPermissionException() {}

    public DataPermissionException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

    public DataPermissionException(String arg0)
    {
        super(arg0);
    }

    public DataPermissionException(Throwable arg0)
    {
        super(arg0);
    }
}
