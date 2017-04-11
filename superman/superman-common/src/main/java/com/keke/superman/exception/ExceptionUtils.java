package com.keke.superman.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class ExceptionUtils
{
    static final String WRAPPED_MARKER = " [wrapped] ";
    private static String[] CAUSE_METHOD_NAMES = { "getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested" };
    private static final Method THROWABLE_CAUSE_METHOD;

    static
    {
        Method localMethod;
        try
        {
            localMethod = Throwable.class.getMethod("getCause", null);
        }
        catch (Exception localException)
        {
            localMethod = null;
        }
        THROWABLE_CAUSE_METHOD = localMethod;
    }

    public static void addCauseMethodName(String methodName)
    {
        if (StringUtils.isNotEmpty(methodName))
        {
            ArrayList localArrayList = new ArrayList(Arrays.asList(CAUSE_METHOD_NAMES));
            localArrayList.add(methodName);
            CAUSE_METHOD_NAMES = (String[])localArrayList.toArray(new String[localArrayList.size()]);
        }
    }

    public static Throwable getCause(Throwable throwable)
    {
        return getCause(throwable, CAUSE_METHOD_NAMES);
    }

    public static Throwable getCause(Throwable throwable, String[] methodNames)
    {
        if (throwable == null) {
            return null;
        }
        Throwable localThrowable = getCauseUsingWellKnownTypes(throwable);
        if (localThrowable == null)
        {
            if (methodNames == null) {
                methodNames = CAUSE_METHOD_NAMES;
            }
            for (int i = 0; i < methodNames.length; i++)
            {
                String str = methodNames[i];
                if (str != null)
                {
                    localThrowable = getCauseUsingMethodName(throwable, str);
                    if (localThrowable != null) {
                        break;
                    }
                }
            }
            if (localThrowable == null) {
                localThrowable = getCauseUsingFieldName(throwable, "detail");
            }
        }
        return localThrowable;
    }

    public static Throwable getRootCause(Throwable throwable)
    {
        Throwable localThrowable = getCause(throwable);
        if (localThrowable != null)
        {
            throwable = localThrowable;
            while ((throwable = getCause(throwable)) != null) {
                localThrowable = throwable;
            }
        }
        return localThrowable;
    }

    private static Throwable getCauseUsingWellKnownTypes(Throwable throwable)
    {
        if ((throwable instanceof Nestable)) {
            return throwable.getCause();
        }
        if ((throwable instanceof SQLException)) {
            return ((SQLException)throwable).getNextException();
        }
        if ((throwable instanceof InvocationTargetException)) {
            return ((InvocationTargetException)throwable).getTargetException();
        }
        return null;
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName)
    {
        Method localMethod = null;
        try
        {
            localMethod = throwable.getClass().getMethod(methodName, null);
        }
        catch (NoSuchMethodException localNoSuchMethodException) {}catch (SecurityException localSecurityException) {}
        if ((localMethod != null) && (Throwable.class.isAssignableFrom(localMethod.getReturnType()))) {
            try
            {
                return (Throwable)localMethod.invoke(throwable, ArrayUtils.EMPTY_OBJECT_ARRAY);
            }
            catch (IllegalAccessException localIllegalAccessException) {}catch (IllegalArgumentException localIllegalArgumentException) {}catch (InvocationTargetException localInvocationTargetException) {}
        }
        return null;
    }

    private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName)
    {
        Field localField = null;
        try
        {
            localField = throwable.getClass().getField(fieldName);
        }
        catch (NoSuchFieldException localNoSuchFieldException) {}catch (SecurityException localSecurityException) {}
        if ((localField != null) && (Throwable.class.isAssignableFrom(localField.getType()))) {
            try
            {
                return (Throwable)localField.get(throwable);
            }
            catch (IllegalAccessException localIllegalAccessException) {}catch (IllegalArgumentException localIllegalArgumentException) {}
        }
        return null;
    }

    public static boolean isThrowableNested()
    {
        return THROWABLE_CAUSE_METHOD != null;
    }

    public static boolean isNestedThrowable(Throwable throwable)
    {
        if (throwable == null) {
            return false;
        }
        if ((throwable instanceof Nestable)) {
            return true;
        }
        if ((throwable instanceof SQLException)) {
            return true;
        }
        if ((throwable instanceof InvocationTargetException)) {
            return true;
        }
        if (isThrowableNested()) {
            return true;
        }
        Class localClass = throwable.getClass();
        int i = 0;
        for (int j = CAUSE_METHOD_NAMES.length; i < j; i++) {
            try
            {
                Method localMethod = localClass.getMethod(CAUSE_METHOD_NAMES[i], null);
                if ((localMethod != null) && (Throwable.class.isAssignableFrom(localMethod.getReturnType()))) {
                    return true;
                }
            }
            catch (NoSuchMethodException localNoSuchMethodException1) {}catch (SecurityException localSecurityException3) {}
        }
        try
        {
            Field localField = localClass.getField("detail");
            if (localField != null) {
                return true;
            }
        }
        catch (NoSuchFieldException localNoSuchFieldException1) {}catch (SecurityException localSecurityException2) {}
        return false;
    }

    public static int getThrowableCount(Throwable throwable)
    {
        int i = 0;
        while (throwable != null)
        {
            i++;
            throwable = getCause(throwable);
        }
        return i;
    }

    public static Throwable[] getThrowables(Throwable throwable)
    {
        ArrayList localArrayList = new ArrayList();
        while (throwable != null)
        {
            localArrayList.add(throwable);
            throwable = getCause(throwable);
        }
        return (Throwable[])localArrayList.toArray(new Throwable[localArrayList.size()]);
    }

    public static int indexOfThrowable(Throwable throwable, Class type)
    {
        return indexOfThrowable(throwable, type, 0);
    }

    public static int indexOfThrowable(Throwable throwable, Class type, int fromIndex)
    {
        if (throwable == null) {
            return -1;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        Throwable[] arrayOfThrowable = getThrowables(throwable);
        if (fromIndex >= arrayOfThrowable.length) {
            return -1;
        }
        for (int i = fromIndex; i < arrayOfThrowable.length; i++) {
            if (arrayOfThrowable[i].getClass().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    public static void printRootCauseStackTrace(Throwable throwable)
    {
        printRootCauseStackTrace(throwable, System.err);
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintStream stream)
    {
        if (throwable == null) {
            return;
        }
        if (stream == null) {
            throw new IllegalArgumentException("The PrintStream must not be null");
        }
        String[] arrayOfString = getRootCauseStackTrace(throwable);
        for (int i = 0; i < arrayOfString.length; i++) {
            stream.println(arrayOfString[i]);
        }
        stream.flush();
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintWriter writer)
    {
        if (throwable == null) {
            return;
        }
        if (writer == null) {
            throw new IllegalArgumentException("The PrintWriter must not be null");
        }
        String[] arrayOfString = getRootCauseStackTrace(throwable);
        for (int i = 0; i < arrayOfString.length; i++) {
            writer.println(arrayOfString[i]);
        }
        writer.flush();
    }

    public static String[] getRootCauseStackTrace(Throwable throwable)
    {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        Throwable[] arrayOfThrowable = getThrowables(throwable);
        int i = arrayOfThrowable.length;
        ArrayList localArrayList = new ArrayList();
        List localList1 = getStackFrameList(arrayOfThrowable[(i - 1)]);
        int j = i;
        for (;;)
        {
            j--;
            if (j < 0) {
                break;
            }
            List localList2 = localList1;
            if (j != 0)
            {
                localList1 = getStackFrameList(arrayOfThrowable[(j - 1)]);
                removeCommonFrames(localList2, localList1);
            }
            if (j == i - 1) {
                localArrayList.add(arrayOfThrowable[j].toString());
            } else {
                localArrayList.add(" [wrapped] " + arrayOfThrowable[j].toString());
            }
            for (int k = 0; k < localList2.size(); k++) {
                localArrayList.add(localList2.get(k));
            }
        }
        return (String[])localArrayList.toArray(new String[0]);
    }

    public static void removeCommonFrames(List causeFrames, List wrapperFrames)
    {
        if ((causeFrames == null) || (wrapperFrames == null)) {
            throw new IllegalArgumentException("The List must not be null");
        }
        int i = causeFrames.size() - 1;
        int j = wrapperFrames.size() - 1;
        while ((i >= 0) && (j >= 0))
        {
            String str1 = (String)causeFrames.get(i);
            String str2 = (String)wrapperFrames.get(j);
            if (str1.equals(str2)) {
                causeFrames.remove(i);
            }
            i--;
            j--;
        }
    }

    public static String getStackTrace(Throwable throwable)
    {
        StringWriter localStringWriter = new StringWriter();
        PrintWriter localPrintWriter = new PrintWriter(localStringWriter, true);
        throwable.printStackTrace(localPrintWriter);
        return localStringWriter.getBuffer().toString();
    }

    public static String getFullStackTrace(Throwable throwable)
    {
        StringWriter localStringWriter = new StringWriter();
        PrintWriter localPrintWriter = new PrintWriter(localStringWriter, true);
        Throwable[] arrayOfThrowable = getThrowables(throwable);
        for (int i = 0; i < arrayOfThrowable.length; i++)
        {
            arrayOfThrowable[i].printStackTrace(localPrintWriter);
            if (isNestedThrowable(arrayOfThrowable[i])) {
                break;
            }
        }
        return localStringWriter.getBuffer().toString();
    }

    public static String[] getStackFrames(Throwable throwable)
    {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return getStackFrames(getStackTrace(throwable));
    }

    static String[] getStackFrames(String stackTrace)
    {
        String str = SystemUtils.LINE_SEPARATOR;
        StringTokenizer localStringTokenizer = new StringTokenizer(stackTrace, str);
        LinkedList localLinkedList = new LinkedList();
        while (localStringTokenizer.hasMoreTokens()) {
            localLinkedList.add(localStringTokenizer.nextToken());
        }
        return (String[])localLinkedList.toArray(new String[localLinkedList.size()]);
    }

    static List getStackFrameList(Throwable t)
    {
        String str1 = getStackTrace(t);
        String str2 = SystemUtils.LINE_SEPARATOR;
        StringTokenizer localStringTokenizer = new StringTokenizer(str1, str2);
        LinkedList localLinkedList = new LinkedList();
        int i = 0;
        while (localStringTokenizer.hasMoreTokens())
        {
            String str3 = localStringTokenizer.nextToken();

            int j = str3.indexOf("at");
            if ((j != -1) && (str3.substring(0, j).trim().length() == 0))
            {
                i = 1;
                localLinkedList.add(str3);
            }
            else
            {
                if (i != 0) {
                    break;
                }
            }
        }
        return localLinkedList;
    }
}

