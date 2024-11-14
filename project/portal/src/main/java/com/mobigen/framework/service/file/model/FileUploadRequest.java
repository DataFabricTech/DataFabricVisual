package com.mobigen.framework.service.file.model;

import java.util.List;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadRequest {
	private List<MultipartFile> files;
	private List<MultipartFile> thumb_img;
	private Boolean delete_thumb_flag;
	private List<MultipartFile> upload;
	private String type;
	private PostVO post;
	private Integer post_id;
	private List<String> delete_files;
	private DataRequestVO dataRequest;
	private Integer dataRequestId;
}
