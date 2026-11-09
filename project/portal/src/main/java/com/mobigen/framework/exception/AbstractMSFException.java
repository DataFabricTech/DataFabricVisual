package com.mobigen.framework.exception;

@SuppressWarnings("serial")
public class AbstractMSFException extends Exception {

	private String exceptionMessage;

	public AbstractMSFException() {
	}

	public AbstractMSFException(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public AbstractMSFException(String exceptionMessage, Throwable e) {
		this.exceptionMessage = exceptionMessage;
		this.initCause(e);
	}

	public String getExceptionMessage() {
		return this.exceptionMessage;
	}
}
