package com.keke.superman.validation.utils;

import com.baidu.disconf.client.usertools.DisconfDataGetter;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public final class ValidationJdbcTemplateHolder
{
    private static Map<String, Object> a;
    private static DataSource b;
    private static JdbcTemplate c;

    public static Map<String, Object> getValidationConfig()
    {
        if (a == null) {
            a = DisconfDataGetter.getByFile("validation.properties");
        }
        return a;
    }

    public static DataSource getValidationDataSource(ApplicationContext appContext)
    {
        if (b == null)
        {
            String str = (String)getValidationConfig().get("validation.i18n.messages.datasource.name");
            if (StringUtils.isEmpty(str)) {
                str = "dataSource";
            }
            b = (DataSource)appContext.getBean(str, DataSource.class);
        }
        return b;
    }

    public static JdbcTemplate getValidationJdbcTemplate(ApplicationContext applicationContext)
    {
        if (c == null)
        {
            DataSource localDataSource = getValidationDataSource(applicationContext);
            c = new JdbcTemplate(localDataSource);
        }
        return c;
    }
}
