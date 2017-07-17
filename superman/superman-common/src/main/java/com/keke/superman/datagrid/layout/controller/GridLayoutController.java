package com.keke.superman.datagrid.layout.controller;

import java.awt.*;
import java.util.List;

import com.keke.superman.datagrid.layout.domain.GridLayout;
import com.keke.superman.datagrid.layout.domain.GridLayoutRequest;
import com.keke.superman.datagrid.layout.service.GridLayoutService;
import com.keke.superman.datagrid.layout.util.LayoutUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/layout"})
public class GridLayoutController
        implements ApplicationContextAware
{
    private GridLayoutService a;

    @ResponseBody
    @RequestMapping({"/list/{tableId}"})
    public ResponseEntity<List<GridLayout>> listLayout(@PathVariable("tableId") String tableId, String pathname)
    {
        if ((StringUtils.isEmpty(tableId)) || (StringUtils.isEmpty(pathname))) {
            return null;
        }
        String str = tableId + LayoutUtils.getPageUrlEncode(pathname);
        List localList = this.a.findAllLayout(str);
        return new ResponseEntity(localList, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping({"/get/{tableId}"})
    public ResponseEntity<GridLayout> getLayout(@PathVariable("tableId") String tableId, String pathname)
    {
        if ((StringUtils.isEmpty(tableId)) || (StringUtils.isEmpty(pathname))) {
            return null;
        }
        String str = tableId + LayoutUtils.getPageUrlEncode(pathname);
        GridLayout localGridLayout = this.a.findLastLayout(str);
        return new ResponseEntity(localGridLayout, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping({"/get/{tableId}/{name}"})
    public ResponseEntity<GridLayout> getLayout(@PathVariable("tableId") String tableId, @PathVariable("name") String name, String pathname)
    {
        if ((StringUtils.isEmpty(tableId)) || (StringUtils.isEmpty(pathname))) {
            return null;
        }
        String str = tableId + LayoutUtils.getPageUrlEncode(pathname);
        GridLayout localGridLayout = this.a.findLayoutByName(str, name);
        return new ResponseEntity(localGridLayout, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping({"/delete/{tableId}/{name}"})
    public ResponseEntity deleteLayout(@PathVariable("tableId") String tableId, @PathVariable("name") String name, String pathname)
    {
        if ((StringUtils.isEmpty(tableId)) || (StringUtils.isEmpty(pathname))) {
            return new ResponseEntity(null, HttpStatus.OK);
        }
        String str = tableId + LayoutUtils.getPageUrlEncode(pathname);
        this.a.deleteLayout(str, name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value={"/persist"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public ResponseEntity<GridLayout> saveLayout(@RequestBody GridLayoutRequest gridLayoutRequest)
    {
        String str1 = gridLayoutRequest.getTableId();
        if (StringUtils.isEmpty(str1)) {
            return null;
        }
        String str2 = str1 + LayoutUtils.getPageUrlEncode(gridLayoutRequest.getPathname());
        GridLayout localGridLayout = new GridLayout();
        localGridLayout.setName(gridLayoutRequest.getName());
        localGridLayout.setTableId(str2);
        localGridLayout.setContent(gridLayoutRequest.getContent());
        this.a.saveOrUpdateLayout(localGridLayout);
        return new ResponseEntity(localGridLayout, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping({"/delete/{tableId}"})
    public ResponseEntity deleteAllLayout(@PathVariable("tableId") String tableId, String pathname)
    {
        if ((StringUtils.isEmpty(tableId)) || (StringUtils.isEmpty(pathname))) {
            return new ResponseEntity(null, HttpStatus.OK);
        }
        String str = tableId + LayoutUtils.getPageUrlEncode(pathname);
        this.a.deleteLayout(str);
        return new ResponseEntity(HttpStatus.OK);
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        GridLayoutService localGridLayoutService;
        try
        {
            localGridLayoutService = (GridLayoutService)applicationContext.getBean("gridLayoutService");
        }
        catch (Exception localException)
        {
            localGridLayoutService = null;
        }
        if (localGridLayoutService == null) {
            this.a = ((GridLayoutService)applicationContext.getBean("defaultGridLayoutService"));
        } else {
            this.a = localGridLayoutService;
        }
    }
}