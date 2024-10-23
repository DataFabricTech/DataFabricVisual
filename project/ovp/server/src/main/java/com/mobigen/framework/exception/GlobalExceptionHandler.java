package com.mobigen.framework.exception;

import com.mobigen.framework.component.Messages;
import com.mobigen.framework.result.JsonResult;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Component
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Messages message;

    // Model Validation
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public JsonResult bindExceptionHandler(BindException e) {
        log.error(message.get("com.mobigen.framework.exception.GlobalExceptionHandler.bindExceptionHandler"), e);
        return getExceptionJsonResult(e.getFieldError());
    }

    // JSON Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(message.get("com.mobigen.framework.exception.GlobalExceptionHandler.methodArgumentNotValidExceptionHandler"), e);

        BindingResult bindingResult = e.getBindingResult();
        return getExceptionJsonResult(bindingResult.getFieldError());
    }

    // Controller
    @ExceptionHandler(JsonResultException.class)
    @ResponseBody
    public JsonResult jsonResultExceptionHandler(JsonResultException e) {
        log.error(message.get("com.mobigen.framework.exception.GlobalExceptionHandler.jsonResultExceptionHandler"), e);

        Throwable cause = e.getCause();
        if (cause instanceof FeignException feignException) {
            return feignExceptionHandler(feignException);
        }

        return getExceptionJsonResult(e);
    }

    // Authentication
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public JsonResult accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error(message.get("com.mobigen.framework.exception.GlobalExceptionHandler.accessDeniedExceptionHandler"), e);
        return getExceptionJsonResult(e);
    }

    // No Static Resource
    @ExceptionHandler(NoResourceFoundException.class)
    public RedirectView noStaticResourceFoundExceptionHandler(NoResourceFoundException e) {
        log.error(message.get("com.mobigen.framework.exception.GlobalExceptionHandler.NoResourceFoundException"), e);
        /**
         * 없는 페이지 접근시에 error 페이지로 넘긴다.
         */
        return new RedirectView("/portal/login/error");
    }

    // Unknown
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult unknownExceptionHandler(Exception e) {
        log.error(message.get("com.mobigen.framework.exception.GlobalExceptionHandler.unknownExceptionHandler"), e);
        return getExceptionJsonResult(e);
    }

    public JsonResult feignExceptionHandler(FeignException ex) {
        log.error("FeignException");

        String errorMessage = "서버 오류가 발생";

        try {
            // Feign 예외 응답 본문 파싱
            String responseBody = ex.contentUTF8();

            if (responseBody != null && !responseBody.isEmpty()) {
                JSONObject json = new JSONObject(responseBody);

                if (json.has("code") && json.getInt("code") == 20001) {
                    errorMessage = "중복된 컬럼이 있습니다.";
                } else {
                    errorMessage = json.toString();
                }
            } else {
                log.warn("Feign 응답 본문이 비어 있음");
            }
        } catch (Exception e) {
            log.error("Feign 응답 처리 중 오류 발생", e);
        }

        JsonResult js = new JsonResult();
        js.setErrorMessage(errorMessage);

        return js;
    }

    private String getMessageKey(FieldError error) {
        String errorCode = "";
        String[] codes = error.getCodes();
        if (codes != null && codes.length > 0) {
            errorCode = codes[0];
        }

        return errorCode;
    }

    private String getDefaultMessage(Object e) {
        String msg = "";
        if (e instanceof FieldError fieldError) {
            msg = fieldError.getDefaultMessage();
        } else if (e instanceof JsonResultException jsonResultException) {
            msg = jsonResultException.getExceptionMessage();
        } else if (e instanceof AccessDeniedException accessDeniedException) {
            msg = accessDeniedException.getLocalizedMessage();
        } else if (e instanceof Exception exception) {
            msg = exception.getMessage();
        }

        if (msg == null || msg.isEmpty()) {
            msg = message.get("com.mobigen.framework.exception.GlobalExceptionHandler");
        }

        return msg;
    }

    private Object[] getArguments(Object e) {
        Object[] arguments = null;
        if (e instanceof FieldError fieldError) {
            arguments = fieldError.getArguments();
        } else if (e instanceof JsonResultException jsonResultException) {
            arguments = jsonResultException.getArgs();
        }

        return arguments;
    }

    private String getMessageKey(Object e) {
        String key = "";

        if (e instanceof FieldError fieldError) {
            key = getMessageKey(fieldError);
        } else if (e instanceof JsonResultException jsonResultException) {
            key = jsonResultException.getMessageKey();
        }

        if (key == null || key.isEmpty()) {
            StackTraceElement element = ((Exception) e).getStackTrace()[0];
            key = element.getClassName() + "." + element.getMethodName();
        }

        return key;
    }

    private JsonResult getExceptionJsonResult(Object error) {
        String msg;
        try {
            String key = getMessageKey(error);
            Object[] arguments = getArguments(error);
            msg = message.get(key, arguments);

            if (msg == null || key.equals(msg) || msg.isEmpty()) {
                msg = getDefaultMessage(error);
            }
        } catch (Exception e) {
            msg = message.get("com.mobigen.framework.exception.GlobalExceptionHandler");
        }

        JsonResult js = new JsonResult();
        js.setErrorMessage(msg);

        // sentry
        // Sentry.captureException(exception, msg);
        return js;
    }

}
