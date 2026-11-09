package com.mobigen.framework.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
@Data
public class JsonResultException extends AbstractMSFException {
	private String messageKey;
	private Object[] args;

	public JsonResultException() {		
	}

	public JsonResultException(String messageKey, Object[] args, Throwable e) {
		this.messageKey = messageKey;
		this.args = args;
		this.initCause(e);
	}
	public JsonResultException(String messageKey, Object[] args) {
		this.messageKey = messageKey;
		this.args = args;
	}
	public JsonResultException(Throwable e) {
		this.initCause(e);
	}
}
