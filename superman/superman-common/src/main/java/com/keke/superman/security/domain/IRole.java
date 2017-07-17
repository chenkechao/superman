package com.keke.superman.security.domain;

import java.util.Collection;

import com.keke.superman.security.data.entity.ITable;
import org.springframework.security.core.GrantedAuthority;

public abstract interface IRole
        extends GrantedAuthority
{
    public abstract Object getRoleId();

    public abstract Collection<ITable> getPermissionTables();
}
