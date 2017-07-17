package com.keke.superman.security;


import com.keke.superman.security.domain.IRole;

public abstract interface RoleInfoCollector
{
    public abstract IRole getCurrentRole();
}
