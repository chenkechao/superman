package com.keke.superman.validation.service;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import com.keke.superman.validation.dao.I18nMessageDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.AbstractMessageSource;

public class I18nMessageSource
        extends AbstractMessageSource
        implements II18nMessageSource, InitializingBean
{
    private I18nMessageDao a;
    private Table<String, Locale, MessageFormat> b;

    protected MessageFormat resolveCode(String code, Locale locale)
    {
        return (MessageFormat)this.b.get(code, locale);
    }

    public void afterPropertiesSet()
            throws Exception
    {
        ImmutableTable.Builder localBuilder = ImmutableTable.builder();
        List localList = this.a.query();
//        for (ae localae : localList)
//        {
//            MessageFormat localMessageFormat = new MessageFormat(StringUtils.defaultString(localae.c()), localae.b());
//            localBuilder.put(localae.a(), localae.b(), localMessageFormat);
//        }
        this.b = localBuilder.build();
    }

    public final Table<String, String, String> getNlsMessagesTable(final Iterable<String> messageKeys)
    {
//        Iterable localIterable = Iterables.filter(this.b.cellSet(), new Predicate()
//        {
//            public boolean apply(Table.Cell<String, Locale, MessageFormat> input)
//            {
//                return Iterables.contains(messageKeys, input.getRowKey());
//            }
//        });
//        HashBasedTable localHashBasedTable = HashBasedTable.create();
//        for (Table.Cell localCell : localIterable)
//        {
//            String str = ((Locale)localCell.getColumnKey()).toString().toLowerCase();
//            str = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, str);
//            localHashBasedTable.put(localCell.getRowKey(), str, ((MessageFormat)localCell.getValue()).toPattern());
//        }
//        return localHashBasedTable;
        return null;
    }

    public void setI18nMessageDao(I18nMessageDao i18nMessageDao)
    {
        this.a = i18nMessageDao;
    }

    public Table<String, Locale, MessageFormat> getI18nMessageTable()
    {
        return this.b;
    }
}