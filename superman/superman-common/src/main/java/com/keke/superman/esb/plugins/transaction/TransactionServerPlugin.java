package com.keke.superman.esb.plugins.transaction;

import com.keke.superman.esb.plugins.ServerPluginAdapter;
import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.util.RequestAndResponseContextHolder;

import java.util.Properties;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class TransactionServerPlugin
        extends ServerPluginAdapter
{
    public void afterRequest(Request request)
    {
        if ((request.getHeader() != null) &&
                (request.getHeader().getProperties() != null) &&
                (request.getHeader().getProperties().containsKey("transactionToken")))
        {
            String str = (String)request.getHeader().getProperties().get("transactionToken");
            HttpServletRequest localHttpServletRequest = RequestAndResponseContextHolder.request();
            localHttpServletRequest.setAttribute("esb-transaction-token", str);
        }
    }
}
