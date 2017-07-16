package com.keke.superman.validation.web.advice;

import com.google.common.base.CaseFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.keke.superman.esb.util.JsonMapperHolder;
import com.keke.superman.esb.util.RequestAndResponseContextHolder;
import com.keke.superman.esb.util.ResponseUtils;
import com.keke.superman.web.vo.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springside.modules.mapper.JsonMapper;

@ControllerAdvice
public class ValidationErrorHandler
{
    @Autowired
    @Qualifier("i18nMessageSource")
    private MessageSource a;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public void processValidationError(MethodArgumentNotValidException ex)
    {
        HttpServletResponse localHttpServletResponse = RequestAndResponseContextHolder.response();
        BindingResult localBindingResult = ex.getBindingResult();
        List localList = localBindingResult.getFieldErrors();
        ValidationError localValidationError = a(localList);
        ResponseUtils.renderJson(localHttpServletResponse, JsonMapperHolder.jsonMapper.toJson(localValidationError));
    }

    private ValidationError a(List<FieldError> paramList)
    {
        ValidationError localValidationError = new ValidationError();
        for (FieldError localFieldError : paramList)
        {
            String str1 = localFieldError.getField();
            String str2 = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, localFieldError.getCode());
            String str3 = localFieldError.getCodes()[0];
            localValidationError.addFieldError(str1, str2, str3);
        }
        return localValidationError;
    }
}