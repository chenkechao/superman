package com.keke.superman.security.data.entity;

public abstract interface IDataPermission
{
    public abstract String getConditionSql();

    public abstract CrudType getCrudType();
}
