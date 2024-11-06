package com.mobigen.framework.service.file.model;

import lombok.Data;

@Data
public class FileUploadResponse {
	private int fileNum;
	private int file_id;
	private String fileLocation;
	private String originalFileName;
	private String saveFileName;
	private String fileName;
	private String type;
	private String regUser;
	long fileSize;
	Integer post_id;
	String file_type;
	
	public FileUploadResponse() {
	}

	public FileUploadResponse(String originalFileName, String saveFileName, String type) {
		this.originalFileName = originalFileName;
		this.saveFileName = saveFileName;
		this.type = type;
	}
	public FileUploadResponse(String originalFileName, String saveFileName, String type, String regUser, long fileSize) {
		this.originalFileName = originalFileName;
		this.saveFileName = saveFileName;
		this.type = type;
		this.regUser = regUser;
		this.fileSize = fileSize;
	}
	public FileUploadResponse(String originalFileName, String saveFileName, String type, String regUser, long fileSize, int file_id) {
		this.originalFileName = originalFileName;
		this.saveFileName = saveFileName;
		this.type = type;
		this.regUser = regUser;
		this.fileSize = fileSize;
		this.file_id = file_id;
	}
}

