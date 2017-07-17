package com.keke.superman.security.data.entity;


import java.util.Collection;

public abstract interface ITable
{
    public abstract String getTableName();

    public abstract AclMode getAclMode();

    public abstract Collection<IDataPermission> getDataPermisssions();
}
