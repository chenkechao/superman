package com.keke.superman.exception;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class NestableDelegate
        implements Serializable
{
    private static final long serialVersionUID = 4050197523027210545L;
    private static final transient String MUST_BE_THROWABLE = "The Nestable implementation passed to the NestableDelegate(Nestable) constructor must extend java.lang.Throwable";
    private Throwable nestable = null;
    public static boolean topDown = true;
    public static boolean trimStackFrames = true;

    public NestableDelegate(Nestable nestable)
    {
        if ((nestable instanceof Throwable)) {
            this.nestable = ((Throwable)nestable);
        } else {
            throw new IllegalArgumentException("The Nestable implementation passed to the NestableDelegate(Nestable) constructor must extend java.lang.Throwable");
        }
    }

    public String getMessage(int index)
    {
        Throwable localThrowable = getThrowable(index);
        if (Nestable.class.isInstance(localThrowable)) {
            return ((Nestable)localThrowable).getMessage(0);
        }
        return localThrowable.getMessage();
    }

    public String getMessage(String baseMsg)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        if (baseMsg != null) {
            localStringBuffer.append(baseMsg);
        }
        Throwable localThrowable = ExceptionUtils.getCause(this.nestable);
        if (localThrowable != null)
        {
            String str = localThrowable.getMessage();
            if (str != null)
            {
                if (baseMsg != null) {
                    localStringBuffer.append(": ");
                }
                localStringBuffer.append(str);
            }
        }
        return localStringBuffer.length() > 0 ? localStringBuffer.toString() : null;
    }

    public String[] getMessages()
    {
        Throwable[] arrayOfThrowable = getThrowables();
        String[] arrayOfString = new String[arrayOfThrowable.length];
        for (int i = 0; i < arrayOfThrowable.length; i++) {
            arrayOfString[i] = (Nestable.class.isInstance(arrayOfThrowable[i]) ? ((Nestable)arrayOfThrowable[i]).getMessage(0) : arrayOfThrowable[i].getMessage());
        }
        return arrayOfString;
    }

    public Throwable getThrowable(int index)
    {
        if (index == 0) {
            return this.nestable;
        }
        Throwable[] arrayOfThrowable = getThrowables();
        return arrayOfThrowable[index];
    }

    public int getThrowableCount()
    {
        return ExceptionUtils.getThrowableCount(this.nestable);
    }

    public Throwable[] getThrowables()
    {
        return ExceptionUtils.getThrowables(this.nestable);
    }

    public int indexOfThrowable(Class type, int fromIndex)
    {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("The start index was out of bounds: " + fromIndex);
        }
        Throwable[] arrayOfThrowable = ExceptionUtils.getThrowables(this.nestable);
        if (fromIndex >= arrayOfThrowable.length) {
            throw new IndexOutOfBoundsException("The start index was out of bounds: " + fromIndex + " >= " + arrayOfThrowable.length);
        }
        for (int i = fromIndex; i < arrayOfThrowable.length; i++) {
            if (arrayOfThrowable[i].getClass().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    public void printStackTrace()
    {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream out)
    {
        synchronized (out)
        {
            PrintWriter localPrintWriter = new PrintWriter(out, false);
            printStackTrace(localPrintWriter);

            localPrintWriter.flush();
        }
    }

    public void printStackTrace(PrintWriter out)
    {
        Throwable localThrowable = this.nestable;
        if (ExceptionUtils.isThrowableNested())
        {
            if ((localThrowable instanceof Nestable)) {
                ((Nestable)localThrowable).printPartialStackTrace(out);
            } else {
                localThrowable.printStackTrace(out);
            }
            return;
        }
        Object localObject1 = null;//TODO
        ArrayList localArrayList = new ArrayList();
        while (localThrowable != null)
        {
            localObject1 = getStackFrames(localThrowable);
            localArrayList.add(localObject1);
            localThrowable = ExceptionUtils.getCause(localThrowable);
        }
        localObject1 = "Caused by: ";
        if (!topDown)
        {
            localObject1 = "Rethrown as: ";
            Collections.reverse(localArrayList);
        }
        if (trimStackFrames) {
            trimStackFrames(localArrayList);
        }
        Iterator localIterator;
        synchronized (out)
        {
            for (localIterator = localArrayList.iterator(); localIterator.hasNext();)
            {
                String[] arrayOfString = (String[])localIterator.next();
                int i = 0;
                for (int j = arrayOfString.length; i < j; i++) {
                    out.println(arrayOfString[i]);
                }
                if (localIterator.hasNext()) {
                    out.print((String)localObject1);
                }
            }
        }
    }

    protected String[] getStackFrames(Throwable t)
    {
        StringWriter localStringWriter = new StringWriter();
        PrintWriter localPrintWriter = new PrintWriter(localStringWriter, true);
        if ((t instanceof Nestable)) {
            ((Nestable)t).printPartialStackTrace(localPrintWriter);
        } else {
            t.printStackTrace(localPrintWriter);
        }
        return ExceptionUtils.getStackFrames(localStringWriter.getBuffer().toString());
    }

    protected void trimStackFrames(List stacks)
    {
        int i = stacks.size();
        for (int j = i - 1; j > 0; j--)
        {
            String[] arrayOfString1 = (String[])stacks.get(j);
            String[] arrayOfString2 = (String[])stacks.get(j - 1);

            ArrayList localArrayList1 = new ArrayList(Arrays.asList(arrayOfString1));
            ArrayList localArrayList2 = new ArrayList(Arrays.asList(arrayOfString2));
            ExceptionUtils.removeCommonFrames(localArrayList1, localArrayList2);

            int k = arrayOfString1.length - localArrayList1.size();
            if (k > 0)
            {
                localArrayList1.add("\t... " + k + " more");
                stacks.set(j, localArrayList1

                        .toArray(new String[localArrayList1.size()]));
            }
        }
    }
}

