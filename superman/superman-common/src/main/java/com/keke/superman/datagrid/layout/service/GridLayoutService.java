package com.keke.superman.datagrid.layout.service;

import com.keke.superman.datagrid.layout.domain.GridLayout;

import java.util.List;

public abstract interface GridLayoutService
{
    public abstract GridLayout saveOrUpdateLayout(GridLayout paramGridLayout);

    public abstract GridLayout saveLayout(GridLayout paramGridLayout);

    public abstract GridLayout updateLayout(GridLayout paramGridLayout);

    public abstract List<GridLayout> findAllLayout(String paramString);

    public abstract GridLayout findLastLayout(String paramString);

    public abstract GridLayout findLayoutByName(String paramString1, String paramString2);

    public abstract void deleteLayout(String paramString);

    public abstract void deleteLayout(String paramString1, String paramString2);
}
