package com.keke.superman.converter;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {
    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

    public Date convert(String source) {
        ISO8601DateFormat localISO8601DateFormat = new ISO8601DateFormat();
        try
        {
            if (StringUtils.isNoneBlank(new CharSequence[] { source })) {
                return localISO8601DateFormat.parse(source);
            }
            return null;
        } catch (ParseException localParseException) {
            logger.error("日期字符串:{}转化日期对象失败", source);
        }
        return null;
    }
}
