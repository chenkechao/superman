package com.keke.superman.security.domain;

import java.util.Collection;
import java.util.Set;

import com.keke.superman.security.data.entity.ITable;
import org.springframework.security.core.userdetails.UserDetails;

public abstract interface IUser
        extends UserDetails
{
    public abstract Object getUserId();

    public abstract Collection<IRole> getAuthorities();

    public abstract void setAuthorities(Iterable<? extends IRole> paramIterable);

    public abstract Set<String> getHasPermissionUrlPatterns();

    public abstract Collection<ITable> getPermissionTables();
}
