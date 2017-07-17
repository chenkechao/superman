package com.keke.superman.security;

import com.keke.superman.security.domain.IUser;

public abstract interface UserInfoCollector
{
    public abstract Object getUserId();

    public abstract String getOfficeId();

    public abstract IUser getCurrentLoginUser();
}
