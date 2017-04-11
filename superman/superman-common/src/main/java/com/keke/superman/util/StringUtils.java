package com.keke.superman.util;

import com.keke.superman.exception.SystemRuntimeException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class StringUtils
        extends org.apache.commons.lang3.StringUtils
{
    private static final String a = "ISO-8859-1";
    private static final String b = "GB2312";

    public static String firstCharToLowCase(String str)
    {
        String str1 = "";
        if ((str != null) && (str.length() > 0))
        {
            String str2 = str.substring(0, 1);
            str2 = str2.toLowerCase();
            str1 = str2 + str.substring(1, str.length());
        }
        return str1;
    }

    public static String firstCharToUpperCase(String str)
    {
        String str1 = "";
        if ((str != null) && (str.length() > 0))
        {
            String str2 = str.substring(0, 1);
            str2 = str2.toUpperCase();
            str1 = str2 + str.substring(1, str.length());
        }
        return str1;
    }

    public static String[] commaDelimitedListToStringArray(String str)
    {
        return delimitedListToStringArray(str, ",");
    }

    public static Set commaDelimitedListToSet(String str)
    {
        TreeSet localTreeSet = new TreeSet();
        String[] arrayOfString = commaDelimitedListToStringArray(str);
        for (int i = 0; i < arrayOfString.length; i++) {
            localTreeSet.add(arrayOfString[i]);
        }
        return localTreeSet;
    }

    public static String[] delimitedListToStringArray(String str, String delimiter)
    {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[] { str };
        }
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        int j = 0;
        while ((j = str.indexOf(delimiter, i)) != -1)
        {
            localArrayList.add(str.substring(i, j));
            i = j + delimiter.length();
        }
        if ((str.length() > 0) && (i <= str.length())) {
            localArrayList.add(str.substring(i));
        }
        return (String[])localArrayList.toArray(new String[localArrayList.size()]);
    }

    public static String deleteSplithSpace(String str)
    {
        return str.replaceAll("[ ]{2,}", " ");
    }

    public static String transformUs7StringToGB2312String(String us7String)
    {
        try
        {
            return new String(us7String.getBytes("ISO-8859-1"), "GB2312");
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
            throw new SystemRuntimeException(localUnsupportedEncodingException.getMessage());
        }
    }

    public static String transformGB2312ToUs7String(String gbString)
    {
        try
        {
            return new String(gbString.getBytes("GB2312"), "ISO-8859-1");
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
            throw new SystemRuntimeException(localUnsupportedEncodingException.getMessage());
        }
    }
}

