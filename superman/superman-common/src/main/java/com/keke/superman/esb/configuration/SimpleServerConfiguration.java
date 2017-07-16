package com.keke.superman.esb.configuration;

import com.google.common.collect.Lists;
import com.keke.superman.esb.plugins.ServerPlugin;

import java.util.List;

public class SimpleServerConfiguration
        implements ServerConfiguration
{
    private String a;
    private List<ServerPlugin> b = Lists.newArrayList();
    private boolean c = false;

    public String getProviderSysId()
    {
        return this.a;
    }

    public boolean isReturnExceptionStack()
    {
        return this.c;
    }

    public void setProviderSysId(String providerSysId)
    {
        this.a = providerSysId;
    }

    public void setReturnExceptionStack(boolean isReturnExceptionStack)
    {
        this.c = isReturnExceptionStack;
    }

    public List<ServerPlugin> getServerPlugins()
    {
        return this.b;
    }

    public void setServerPlugins(List<ServerPlugin> serverPlugins)
    {
        this.b = serverPlugins;
    }
}

