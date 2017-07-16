package com.keke.superman.web.vo;

public class FieldError
{
    private String a;
    private String b;
    private String c;

    public FieldError() {}

    public FieldError(String field, String ruleName, String messageKey)
    {
        this.a = field;
        this.b = ruleName;
        this.c = messageKey;
    }

    public static FieldError of(String field, String ruleName, String messageKey)
    {
        return new FieldError(field, ruleName, messageKey);
    }

    public String getField()
    {
        return this.a;
    }

    public String getRuleName()
    {
        return this.b;
    }

    public String getMessageKey()
    {
        return this.c;
    }

    public String toString()
    {
        return "FieldError{field='" + this.a + '\'' + ", ruleName='" + this.b + '\'' + ", messageKey='" + this.c + '\'' + '}';
    }
}