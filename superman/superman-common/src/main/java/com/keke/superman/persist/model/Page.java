package com.keke.superman.persist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class Page implements Serializable
{
    private int a;
    private int b;
    private int c;
    private int d;
//    private List<? extends BaseModel> e = new ArrayList<BaseModel>();
//    private Map<String, Object> f =new HashMap<String,Object>();
private List<? extends BaseModel> e = new ArrayList<BaseModel>();
    private Map<String, Object> f =new HashMap<String,Object>();

    public Page()
    {
        this.a = 10;

        this.b = 0;

        this.c = 1;
    }

    public Page(int currentPage, int pageSize)
    {
        this.c = currentPage;
        this.a = pageSize;
    }

    public Page(int currentPage, int totalRecord, int pageSize)
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
            return (this.c - 1) * this.a;
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
        return this.e;
    }

    public void setRecords(List<? extends BaseModel> records)
    {
        this.e = records;
    }

    public Map<String, Object> getMoreAttrs()
    {
        //return ImmutableMap.copyOf(this.f);
        return f;
    }

    public void addMoreAttribute(String key, Object value)
    {
        this.f.put(key, value);
    }

    public static class FieldDomain
    {
        public static final String RECORD_START = "recordStart";
        public static final String RECORD_INDEX = "recordIndex";
        public static final String RECORD_END = "recordEnd";
        public static final String PAGE_SIZE = "pageSize";
        public static final String TOTAL_RECORD = "totalRecord";
        public static final String CURRENT_PAGE = "currentPage";
        public static final String TOTAL_PAGE = "totalPage";
        public static final String RECORDS = "records";
    }
}

