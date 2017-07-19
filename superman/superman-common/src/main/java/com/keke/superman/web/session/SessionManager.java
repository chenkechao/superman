package com.keke.superman.web.session;

import java.io.PrintStream;
import javax.servlet.http.HttpSession;

public class SessionManager
{
    public static final String KEY_USER_CODE = "userCode";
    public static final String KEY_OFFICE_CODE = "officeCode";
    private static final ThreadLocal<HttpSession> a = new ThreadLocal();
    public static final String USER_DETAIL_KEY = "userDetail";

    public static void setSession(HttpSession session)
    {
        a.set(session);
    }

    public static HttpSession getSession()
    {
        return (HttpSession)a.get();
    }

    public static void setAttribute(String var, Object value)
    {
        HttpSession localHttpSession = (HttpSession)a.get();
        if (localHttpSession != null) {
            localHttpSession.setAttribute(var, value);
        } else {
            System.err.println("session not exist can not set attribute with key = '" + var + "' value = [" + value + "]");
        }
    }

    public static <T> T getAttribute(String var)
    {
        HttpSession localHttpSession = (HttpSession)a.get();
        if (localHttpSession != null) {
            return (T)localHttpSession.getAttribute(var);
        }
        System.err.println("session not exist could not get attribute '" + var + "'");
        return null;
    }

    public static final void removeAttribute(String attrName)
    {
        HttpSession localHttpSession = (HttpSession)a.get();
        localHttpSession.removeAttribute(attrName);
    }

    public static String getUserCode()
    {
        return (String)getAttribute("userCode");
    }

    public static void setUserCode(String userCode)
    {
        setAttribute("userCode", userCode);
    }

    public static String getOfficeCode()
    {
        return (String)getAttribute("officeCode");
    }

    public static void setOfficeCode(String officeCode)
    {
        setAttribute("officeCode", officeCode);
    }

    public static void clean()
    {
        if (a.get() != null)
        {
            ((HttpSession)a.get()).invalidate();
            a.set(null);
        }
    }
}