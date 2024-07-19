package com.mobigen.framework.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
public class JsonResult {
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_FAIL = 0;
	private int result = RESULT_FAIL;
	private String errorMessage = "";
	private Object data = null;

	public JsonResult(Object value) {
		this.setData(value);
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		this.result = RESULT_FAIL;

		log.error(errorMessage);
	}

	public void setErrorMessage(Exception e) {
		this.errorMessage = e.getMessage();
		this.result = RESULT_FAIL;
		log.error("setErrorMessage {}", errorMessage);
	}

	public void setErrorMessage(String errorMessage, Exception e) {
		this.errorMessage = errorMessage;
		this.result = RESULT_FAIL;
		log.error("setErrorMessage {}, {}", errorMessage, e);
	}

	public void setData(Object data) {
		this.data = data;
		this.result = RESULT_SUCCESS;
	}
}
