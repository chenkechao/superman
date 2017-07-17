package com.keke.superman.validation.model;

public class UniqueVo
{
    private String entityName;
    private String pkValue;
    private String[] fieldNames;
    private String[] fieldValues;

    public String getEntityName()
    {
        return this.entityName;
    }

    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    public String getPkValue()
    {
        return this.pkValue;
    }

    public void setPkValue(String pkValue)
    {
        this.pkValue = pkValue;
    }

    public String[] getFieldNames()
    {
        return this.fieldNames;
    }

    public void setFieldNames(String[] fieldNames)
    {
        this.fieldNames = fieldNames;
    }

    public String[] getFieldValues()
    {
        return this.fieldValues;
    }

    public void setFieldValues(String[] fieldValues)
    {
        this.fieldValues = fieldValues;
    }
}
