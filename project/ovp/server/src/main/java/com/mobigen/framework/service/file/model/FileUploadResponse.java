package com.mobigen.framework.service.file.model;

import lombok.Data;

@Data
public class FileUploadResponse {
	private String originalFileName;
	private String saveFileName;
	private String type;

	public FileUploadResponse() {
	}

	public FileUploadResponse(String originalFileName, String saveFileName, String type) {
		this.originalFileName = originalFileName;
		this.saveFileName = saveFileName;
		this.type = type;
	}
}
