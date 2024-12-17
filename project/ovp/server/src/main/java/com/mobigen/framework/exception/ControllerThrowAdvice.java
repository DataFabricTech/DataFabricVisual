package com.mobigen.framework.exception;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class ControllerThrowAdvice implements ThrowsAdvice {

	@AfterThrowing(pointcut = "execution(public com.mobigen.framework.result.JsonResult *(..))", throwing = "ex")
	public void jsonResultThrowingHandler(JoinPoint thisJoinPoint, Exception ex) throws JsonResultException {
		String messageKey = "";
		Object[] args = null;

		if (ex instanceof JsonResultException jsonResultException) {
			messageKey = jsonResultException.getMessageKey();
			args = jsonResultException.getArgs();
		}

		if ("".equals(messageKey)) {
			messageKey = getMessageKey(thisJoinPoint);
		}

		throw new JsonResultException(messageKey, args, ex);
	}

	@AfterThrowing(pointcut = "@annotation(com.mobigen.framework.result.annotation.ResponseJsonResult) && @annotation(target)", throwing = "ex")
	public void throwingHandler(JoinPoint thisJoinPoint, Exception ex, ResponseJsonResult target) throws JsonResultException {
		String messageKey = getMessageKey(thisJoinPoint);
		String annoErrorMsg = target.errorMessage();
		if (annoErrorMsg != null && !annoErrorMsg.isEmpty()) {
			throw new JsonResultException(messageKey, null, annoErrorMsg, ex);
		}
		throw new JsonResultException(messageKey, null, ex);
	}

	private String getMessageKey(JoinPoint thisJoinPoint) {
		String path = thisJoinPoint.getSignature().toString();

		Pattern p = Pattern.compile("[^\\s]*(?=\\()");
		Matcher matcher = p.matcher(path);
		if (!matcher.find()) {
			return "";
		}

		path = matcher.group(0);
		return path;
	}
}
