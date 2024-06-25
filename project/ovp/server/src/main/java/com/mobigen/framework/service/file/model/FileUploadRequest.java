package com.mobigen.framework.service.file.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileUploadRequest {
	private List<MultipartFile> files;
	private String type;
}
