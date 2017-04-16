package com.keke.superman.persist.service.intinc;


public abstract interface IncrementIdentities<T extends Number>
{
    public abstract T get();
}
