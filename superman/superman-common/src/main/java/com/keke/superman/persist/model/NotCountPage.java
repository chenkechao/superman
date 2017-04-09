package com.keke.superman.persist.model;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class NotCountPage
        extends Page
{
    private int a;
    private int b;
    private int c;
    private int d;
    private Boolean e;
//    private List<? extends BaseModel> f = Lists.newArrayList();
//    private Map<String, Object> g = Maps.newHashMap();
    private List<? extends BaseModel> f = new ArrayList<BaseModel>();
    private Map<String,Object> g = new HashMap();

    public NotCountPage()
    {
        this.a = 10;

        this.b = 0;

        this.c = 1;
    }

    public NotCountPage(int currentPage, int pageSize)
    {
        this.c = currentPage;
        this.a = pageSize;
    }

    public NotCountPage(int currentPage, int totalRecord, int pageSize)
    {
        this.a = 10;

        this.b = 0;

        this.c = 1;

        this.c = currentPage;
        this.b = totalRecord;
        this.a = pageSize;
    }

    public int getRecordIndex()
    {
        if (this.c > 0) {
            return (this.c - 1) * (this.a - 1);
        }
        return 0;
    }

    public int getRecordStart()
    {
        if (this.c > 0) {
            return (this.c - 1) * this.a + 1;
        }
        return 0;
    }

    public int getRecordEnd()
    {
        if (this.c > 0) {
            return this.c * this.a;
        }
        return 0;
    }

    public int getPageSize()
    {
        return this.a;
    }

    public void setPageSize(int pageSize)
    {
        this.a = pageSize;
    }

    public int getTotalRecord()
    {
        return this.b;
    }

    public void setTotalRecord(int totalRecord)
    {
        this.b = totalRecord;
    }

    public int getCurrentPage()
    {
        return this.c;
    }

    public void setCurrentPage(int currentPage)
    {
        this.c = currentPage;
    }

    public int getTotalPage()
    {
        this.d = ((int)Math.floor(this.b * 1.0D / this.a));
        if (this.b % this.a != 0) {
            this.d += 1;
        }
        if (this.d == 0) {
            return 1;
        }
        return this.d;
    }

    public List<? extends BaseModel> getRecords()
    {
        return this.f;
    }

    public void setRecords(List<? extends BaseModel> records)
    {
        this.f = records;
    }

    public Map<String, Object> getMoreAttrs()
    {
        //return ImmutableMap.copyOf(this.g);
        return g;
    }

    public void addMoreAttribute(String key, Object value)
    {
        this.g.put(key, value);
    }

    public Boolean getHasNextPage()
    {
        if (this.e != null) {
            return this.e;
        }
        return Boolean.valueOf(this.c < getTotalPage());
    }

    public void setHasNextPage(Boolean hasNextPage)
    {
        this.e = hasNextPage;
    }

    public static NotCountPage wrap(Page page)
    {
        NotCountPage localNotCountPage = new NotCountPage();
        try {
            BeanUtils.copyProperties(page, localNotCountPage);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return localNotCountPage;
    }
}

