package com.keke.superman.esb.plugins;

import com.keke.superman.esb.request.Request;
import com.keke.superman.esb.response.Response;

public abstract interface ServerPlugin
{
    public abstract void afterRequest(Request paramRequest);

    public abstract void beforeResponse(Response paramResponse);
}

