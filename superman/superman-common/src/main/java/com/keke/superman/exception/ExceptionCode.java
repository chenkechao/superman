package com.keke.superman.exception;

public enum ExceptionCode
{
    DATASOURCE_EXCEPTION_CODE("0100"),  IO_EXCEPTION_CODE("0200"),  UNKNOWN_EXCEPTION_CODE("0000");

    private String code;

    public String getCode()
    {
        return this.code;
    }

    private ExceptionCode(String code)
    {
        this.code = code;
    }
}
