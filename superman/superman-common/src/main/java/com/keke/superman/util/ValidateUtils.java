package com.keke.superman.util;

import com.keke.superman.exception.IgnoreLoggerCustomRuntimeException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class ValidateUtils
{
    private static final String a = "值 %s 不在指定的排除范围 %s-%s 之外";
    private static final String b = "值 %s 不在指定的包含范围 %s-%s 之间";
    private static final String c = "值 %s 不能匹配表达式 %s";
    private static final String d = "被验证的值为空";
    private static final String e = "被验证的表达式不能得到正确的值";
    private static final String f = "被验证的数组有一个空的元素在下标: %d";
    private static final String g = "被验证的的收集器有一个空的元素在下标: %d";
    private static final String h = "被验证的字符串是空字符串或空格";
    private static final String i = "被验证的数组为空";
    private static final String j = "被验证的字符串是空字符串";
    private static final String k = "被验证的收集器为空";
    private static final String l = "被验证的map为空";
    private static final String m = "The validated array index is invalid: %d";
    private static final String n = "The validated character sequence index is invalid: %d";
    private static final String o = "The validated collection index is invalid: %d";
    private static final String p = "验证状态出错";
    private static final String q = "不能将 %s 赋值给 %s";
    private static final String r = "预期类型: %s, 实际: %s";

    public static void isTrue(boolean expression, String message, long value)
    {
        if (!expression) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, new Object[] { Long.valueOf(value) }));
        }
    }

    public static void isTrue(boolean expression, String message, double value)
    {
        if (!expression) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, new Object[] { Double.valueOf(value) }));
        }
    }

    public static void isTrue(boolean expression, String message, Object... values)
    {
        if (!expression) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }

    public static void isTrue(boolean expression)
    {
        if (!expression) {
            throw new IgnoreLoggerCustomRuntimeException("������������������������������");
        }
    }

    public static <T> T notNull(T object)
    {
        return (T)notNull(object, "��������������", new Object[0]);
    }

    public static <T> T notNull(T object, String message, Object... values)
    {
        if (object == null) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return object;
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... values)
    {
        if (array == null) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        if (array.length == 0) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] array)
    {
        return notEmpty(array, "����������������", new Object[0]);
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... values)
    {
        if (collection == null) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        if (collection.isEmpty()) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T notEmpty(T collection)
    {
        return notEmpty(collection, "������������������", new Object[0]);
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... values)
    {
        if (map == null) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        if (map.isEmpty()) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return map;
    }

    public static <T extends Map<?, ?>> T notEmpty(T map)
    {
        return notEmpty(map, "��������map����", new Object[0]);
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values)
    {
        if (chars == null) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        if (chars.length() == 0) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T notEmpty(T chars)
    {
        return notEmpty(chars, "������������������������", new Object[0]);
    }

    public static <T extends CharSequence> T notBlank(T chars, String message, Object... values)
    {
        if (chars == null) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        if (StringUtils.isBlank(chars)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T notBlank(T chars)
    {
        return notBlank(chars, "������������������������������", new Object[0]);
    }

    public static <T> T[] noNullElements(T[] array, String message, Object... values)
    {
        notNull(array);
        for (int i1 = 0; i1 < array.length; i1++) {
            if (array[i1] == null)
            {
                Object[] arrayOfObject = ArrayUtils.add(values, Integer.valueOf(i1));
                throw new IgnoreLoggerCustomRuntimeException(String.format(message, arrayOfObject));
            }
        }
        return array;
    }

    public static <T> T[] noNullElements(T[] array)
    {
        return noNullElements(array, "��������������������������������: %d", new Object[0]);
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable, String message, Object... values)
    {
        notNull(iterable);
        int i1 = 0;
        for (Iterator localIterator = iterable.iterator(); localIterator.hasNext(); i1++) {
            if (localIterator.next() == null)
            {
                Object[] arrayOfObject = ArrayUtils.addAll(values, new Object[] { Integer.valueOf(i1) });
                throw new IgnoreLoggerCustomRuntimeException(String.format(message, arrayOfObject));
            }
        }
        return iterable;
    }

    public static <T extends Iterable<?>> T noNullElements(T iterable)
    {
        return noNullElements(iterable, "����������������������������������: %d", new Object[0]);
    }

    public static <T> T[] validIndex(T[] array, int index, String message, Object... values)
    {
        notNull(array);
        if ((index < 0) || (index >= array.length)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return array;
    }

    public static <T> T[] validIndex(T[] array, int index)
    {
        return validIndex(array, index, "The validated array index is invalid: %d", new Object[] { Integer.valueOf(index) });
    }

    public static <T extends Collection<?>> T validIndex(T collection, int index, String message, Object... values)
    {
        notNull(collection);
        if ((index < 0) || (index >= collection.size())) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return collection;
    }

    public static <T extends Collection<?>> T validIndex(T collection, int index)
    {
        return validIndex(collection, index, "The validated collection index is invalid: %d", new Object[] { Integer.valueOf(index) });
    }

    public static <T extends CharSequence> T validIndex(T chars, int index, String message, Object... values)
    {
        notNull(chars);
        if ((index < 0) || (index >= chars.length())) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
        return chars;
    }

    public static <T extends CharSequence> T validIndex(T chars, int index)
    {
        return validIndex(chars, index, "The validated character sequence index is invalid: %d", new Object[] { Integer.valueOf(index) });
    }

    public static void validState(boolean expression)
    {
        if (!expression) {
            throw new IgnoreLoggerCustomRuntimeException("������������");
        }
    }

    public static void validState(boolean expression, String message, Object... values)
    {
        if (!expression) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }

    public static void matchesPattern(CharSequence input, String pattern)
    {
        if (!Pattern.matches(pattern, input)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format("�� %s �������������� %s", new Object[] { input, pattern }));
        }
    }

    public static void matchesPattern(CharSequence input, String pattern, String message, Object... values)
    {
        if (!Pattern.matches(pattern, input)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }

    public static <T> void inclusiveBetween(T start, T end, Comparable<T> value)
    {
        if ((value.compareTo(start) < 0) || (value.compareTo(end) > 0)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format("�� %s ������������������ %s~%s ����", new Object[] { value, start, end }));
        }
    }

    public static <T> void inclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values)
    {
        if ((value.compareTo(start) < 0) || (value.compareTo(end) > 0)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }

    public static <T> void exclusiveBetween(T start, T end, Comparable<T> value)
    {
        if ((value.compareTo(start) <= 0) || (value.compareTo(end) >= 0)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format("�� %s ������������������ %s~%s ����", new Object[] { value, start, end }));
        }
    }

    public static <T> void exclusiveBetween(T start, T end, Comparable<T> value, String message, Object... values)
    {
        if ((value.compareTo(start) <= 0) || (value.compareTo(end) >= 0)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj)
    {
        if (!type.isInstance(obj)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format("��������: %s, ����: %s", new Object[] { type.getName(), obj == null ? "null" : obj
                    .getClass().getName() }));
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message, Object... values)
    {
        if (!type.isInstance(obj)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }

    public static void isAssignableFrom(Class<?> superType, Class<?> type)
    {
        if (!superType.isAssignableFrom(type)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format("������ %s ������ %s", new Object[] { type == null ? "null" : type.getName(), superType
                    .getName() }));
        }
    }

    public static void isAssignableFrom(Class<?> superType, Class<?> type, String message, Object... values)
    {
        if (!superType.isAssignableFrom(type)) {
            throw new IgnoreLoggerCustomRuntimeException(String.format(message, values));
        }
    }
}

