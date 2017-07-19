package com.keke.superman.validation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.keke.superman.persist.model.Page;
import com.keke.superman.validation.utils.ValidationJdbcTemplateHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class I18nMessageDao
        implements ApplicationContextAware
{
    private JdbcTemplate jdbcTemplate;

    public List<Page> query()
    {
        this.jdbcTemplate.query("SELECT MSG.`KEY`, L.LANGUAGE, L.COUNTRY, L.VARIANT, MSG.MESSAGE FROM i18n_messages MSG LEFT JOIN supported_locale L ON MSG.LOCALE_ID = L.ID WHERE L.STATE = 0", new RowMapper()
        {
            public Page mapRow(ResultSet rs, int rowNum)
                    throws SQLException
            {
                Page localae = new Page();
                //localae.a(rs.getString("KEY"));
                //localae.b(rs.getString("MESSAGE"));
                String str1 = rs.getString("LANGUAGE");
                String str2 = rs.getString("COUNTRY");
                String str3 = rs.getString("VARIANT");
                //localae.a(af.a(str1, str2, str3));
                return localae;
            }
        });
        return null;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        this.jdbcTemplate = ValidationJdbcTemplateHolder.getValidationJdbcTemplate(applicationContext);
    }
}