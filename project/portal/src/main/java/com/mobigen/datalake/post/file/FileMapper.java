package com.mobigen.datalake.post.file;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mobigen.framework.service.file.model.FileUploadResponse;

@Repository("fileMapper")
public interface FileMapper {
    Integer insertFile(FileUploadResponse param) throws Exception;

    Integer deleteFile(String file_id) throws Exception;

    void deleteThumb(Integer post_id) throws Exception;

    List getFileList(String post_id, String file_type) throws Exception;

    Map<String, Object> getFileFullPath(Map<String, Object> param) throws Exception;

    Map<String, Object> getOriginalFileName(String fileId) throws Exception;

    FileUploadResponse selectFileInfo(Map<String, Object> params);

    Map<String, Object> getDataRequestFileInfo(int file_id);

    Integer fineFileId();

    Integer getFileDupCnt(Map<String, Object> params) throws Exception;

    Integer updateFileRegDate(Map<String, Object> params) throws Exception;
}
