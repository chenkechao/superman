package com.keke.superman.datagrid.layout.domain;


import com.keke.superman.persist.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_GRID_LAYOUT")
public class GridLayout
        extends BaseModel
        implements Serializable, Cloneable
{
    private static final long a = 1L;
    @Id
    @GeneratedValue(generator="idGenerator")
    @Column(name="sys_grid_layout_id", length=50)
    private String b;
    @Column(name="user_id", length=50)
    private Object c;
    @Column(name="role_id", length=50)
    private String d;
    @Column(name="table_id", length=50)
    private String e;
    @Column(name="content")
    private String f;
    @Column(name="name")
    private String g;
    @Column(name="modify")
    private Date h;

    public String getGridLayoutId()
    {
        return this.b;
    }

    public void setGridLayoutId(String gridLayoutId)
    {
        this.b = gridLayoutId;
    }

    public Object getUserId()
    {
        return this.c;
    }

    public void setUserId(Object userId)
    {
        this.c = userId;
    }

    public String getRoleId()
    {
        return this.d;
    }

    public void setRoleId(String roleId)
    {
        this.d = roleId;
    }

    public String getTableId()
    {
        return this.e;
    }

    public void setTableId(String tableId)
    {
        this.e = tableId;
    }

    public String getContent()
    {
        return this.f;
    }

    public void setContent(String content)
    {
        this.f = content;
    }

    public String getName()
    {
        return this.g;
    }

    public void setName(String name)
    {
        this.g = name;
    }

    public Date getModify()
    {
        return this.h;
    }

    public void setModify(Date modify)
    {
        this.h = modify;
    }
}