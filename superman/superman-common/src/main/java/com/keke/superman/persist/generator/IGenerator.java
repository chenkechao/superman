package com.keke.superman.persist.generator;

public abstract interface IGenerator<T>
{
    public abstract T generate(Class<?> paramClass);
}
