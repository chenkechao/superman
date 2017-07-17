package com.keke.superman.validation.dao;

import com.keke.superman.validation.utils.ValidationJdbcTemplateHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;

public class UniqueDao
        implements ApplicationContextAware {
    private JdbcTemplate a;

    public int count(String sql, Object[] args) {
        return ((Integer) this.a.queryForObject(sql, args, Integer.class)).intValue();
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.a = ValidationJdbcTemplateHolder.getValidationJdbcTemplate(applicationContext);
    }
}