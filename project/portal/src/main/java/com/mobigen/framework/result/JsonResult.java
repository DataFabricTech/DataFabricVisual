package com.mobigen.framework.result;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonResult {
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_FAIL = 0;

	public JsonResult() {

	}

	public JsonResult(Object value) {
		this.setData(value);
	}

	private int result = RESULT_FAIL;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	private String errorMessage = "";

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		this.result = RESULT_FAIL;

		log.error(errorMessage);
	}

	public void setErrorMessage(Exception e) {
		this.errorMessage = e.getMessage();
		log.error(errorMessage);
		this.result = RESULT_FAIL;
	}

	public void setErrorMessage(String errorMessage, Exception e) {
		this.errorMessage = errorMessage;
		this.result = RESULT_FAIL;
		log.error(errorMessage, e);
	}

	private Object data = null;

	public Object getData() {
		return data;
	}

	public void setData(Object value) {
		data = value;
		this.result = RESULT_SUCCESS;
	}
}
