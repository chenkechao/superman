package com.keke.superman.validation.service;

import com.google.common.collect.Table;
import java.text.MessageFormat;
import java.util.Locale;
import org.springframework.context.HierarchicalMessageSource;

public abstract interface II18nMessageSource
        extends HierarchicalMessageSource
{
    public abstract Table<String, Locale, MessageFormat> getI18nMessageTable();
}
