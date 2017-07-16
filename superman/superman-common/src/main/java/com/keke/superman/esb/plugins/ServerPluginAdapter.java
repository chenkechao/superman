package com.keke.superman.esb.plugins;


import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.response.Response;

public class ServerPluginAdapter
        implements ServerPlugin
{
    public void afterRequest(Request request) {}

    public void beforeResponse(Response response) {}
}
