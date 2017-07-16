package com.keke.superman.configuration;

import com.keke.superman.esb.plugins.ServerPlugin;

import java.util.List;

public abstract interface ServerConfiguration
{
    public abstract String getProviderSysId();

    public abstract boolean isReturnExceptionStack();

    public abstract List<ServerPlugin> getServerPlugins();
}

