package com.keke.superman.validation.service;

import com.google.common.collect.Lists;
import com.keke.superman.persist.model.JpaAnnotatedModelInfo;
import com.keke.superman.util.StringUtils;
import com.keke.superman.validation.dao.UniqueDao;
import com.keke.superman.validation.model.UniqueVo;
import org.springframework.beans.BeanWrapperImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UniqueService
{
    private UniqueDao a;

    public void setUniqueDao(UniqueDao uniqueDao)
    {
        this.a = uniqueDao;
    }

    public boolean validUnique(UniqueVo uniqueVo)
    {
        Class localClass = null;
        try
        {
            localClass = Class.forName(uniqueVo.getEntityName());
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
            localClassNotFoundException.printStackTrace();
            throw new IllegalArgumentException("不支持验证类型");
        }
        JpaAnnotatedModelInfo localClassNotFoundException = JpaAnnotatedModelInfo.of(localClass);
        BeanWrapperImpl localBeanWrapperImpl = new BeanWrapperImpl(localClass);
        String str1 = localClassNotFoundException.getTableName();
        String str2 = localClassNotFoundException.getPkColumnName();
        String str3 = uniqueVo.getPkValue();
        String[] arrayOfString1 = uniqueVo.getFieldNames();
        String[] arrayOfString2 = uniqueVo.getFieldValues();

        int i = 0;
        String str4;
        for (String str : arrayOfString2) {
            if (StringUtils.isNotBlank(str))
            {
                i = 1;
                break;
            }
        }
        if (i == 0) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder("select count(1) from ").append(str1).append(" where 1=1");
        ArrayList localArrayList = Lists.newArrayList();
        if (StringUtils.isNotBlank(str3))
        {
            ((StringBuilder)stringBuilder).append(" and ").append(str2 + "<>?");
            localArrayList.add(a(localBeanWrapperImpl, localClassNotFoundException.getPkFieldName(), str3));
        }
        for (i = 0; i < arrayOfString1.length; i++)
        {
            str4 = arrayOfString1[i];
            String str5 = arrayOfString2[i];
            int m = str4.lastIndexOf(".");
            if (m > -1) {
                str4 = str4.substring(m + 1);
            }
            String str6 = localClassNotFoundException.getColumnNameByFieldName(str4);
            if (StringUtils.isNotBlank(str5))
            {
                ((StringBuilder)stringBuilder).append(" and ").append(str6).append("=?");
                localArrayList.add(a(localBeanWrapperImpl, str4, str5));
            }
            else
            {
                ((StringBuilder)stringBuilder).append(" and (");
                if (localBeanWrapperImpl.getPropertyType(str4) == String.class) {
                    ((StringBuilder)stringBuilder).append(str6).append("='' or ");
                }
                ((StringBuilder)stringBuilder).append(str6).append(" is null)");
            }
        }
        if (this.a.count(((StringBuilder)stringBuilder).toString(), localArrayList.toArray()) > 0) {
        return false;
    }
        return true;
    }

    private Object a(BeanWrapperImpl paramBeanWrapperImpl, String paramString1, String paramString2)
    {
        Class localClass = paramBeanWrapperImpl.getPropertyType(paramString1);
        if (String.class == localClass) {
            return paramString2;
        }
        if ((Long.class == localClass) || (Long.TYPE == localClass)) {
            return Long.valueOf(paramString2);
        }
        if ((Integer.class == localClass) || (Integer.TYPE == localClass)) {
            return Integer.valueOf(paramString2);
        }
        if ((Float.class == localClass) || (Float.TYPE == localClass)) {
            return Float.valueOf(paramString2);
        }
        if ((Double.class == localClass) || (Double.TYPE == localClass)) {
            return Double.valueOf(paramString2);
        }
        if ((Byte.class == localClass) || (Byte.TYPE == localClass)) {
            return Byte.valueOf(paramString2);
        }
        if (BigDecimal.class == localClass) {
            return new BigDecimal(paramString2);
        }
        return null;
    }
}