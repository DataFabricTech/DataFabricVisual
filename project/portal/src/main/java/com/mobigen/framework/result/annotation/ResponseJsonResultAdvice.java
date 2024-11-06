package com.mobigen.framework.result.annotation;

import com.mobigen.framework.result.JsonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseJsonResultAdvice {
    @Around("@annotation(com.mobigen.framework.result.annotation.ResponseJsonResult)")
    private Object responseJsonResult(ProceedingJoinPoint point) throws Throwable {// NOPMD
        Object value = point.proceed();

        if (value instanceof JsonResult) {
            return value;
        }

        return new JsonResult(value);
    }
}