package com.keke.superman.persist.model;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public abstract class BaseExample
{
    protected Page a;

    public void setPage(Page page)
    {
        this.a = page;
    }

    public Page getPage()
    {
        return this.a;
    }
}

