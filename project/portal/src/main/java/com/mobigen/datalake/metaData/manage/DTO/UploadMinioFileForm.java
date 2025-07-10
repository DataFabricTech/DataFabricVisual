package com.mobigen.datalake.metaData.manage.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UploadMinioFileForm {
    private MultipartFile file;
    private String bucketName;
}
