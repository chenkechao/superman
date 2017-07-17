package com.keke.superman.datagrid.layout.domain;

//import javax.validation.constraints.NotNull;
//import org.hibernate.validator.constraints.NotBlank;

public class GridLayoutRequest
{
//    @NotNull
//    @NotBlank
    private String a;
//    @NotNull
//    @NotBlank
    private String b;
 //   @NotNull
 //   @NotBlank
    private String c;
 //   @NotNull
 //   @NotBlank
    private String d;

    public String getTableId()
    {
        return this.a;
    }

    public void setTableId(String tableId)
    {
        this.a = tableId;
    }

    public String getContent()
    {
        return this.b;
    }

    public void setContent(String content)
    {
        this.b = content;
    }

    public String getName()
    {
        return this.c;
    }

    public void setName(String name)
    {
        this.c = name;
    }

    public String getPathname()
    {
        return this.d;
    }

    public void setPathname(String pathname)
    {
        this.d = pathname;
    }
}
