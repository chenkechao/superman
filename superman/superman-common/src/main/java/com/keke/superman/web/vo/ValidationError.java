package com.keke.superman.web.vo;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidationError
{
    private List<FieldError> a = Lists.newArrayList();
    private List<String> b = Lists.newArrayList();

    public void addFieldError(String field, String ruleName, String messageKey)
    {
        this.a.add(FieldError.of(field, ruleName, messageKey));
    }

    public void addGlobalError(String message)
    {
        this.b.add(message);
    }

    public List<FieldError> getFieldErrors()
    {
        return this.a;
    }

    public List<String> getGlobalErrors()
    {
        return this.b;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}