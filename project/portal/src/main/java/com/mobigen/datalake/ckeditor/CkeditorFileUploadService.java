package com.mobigen.datalake.ckeditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.framework.service.file.FileService;
import com.mobigen.framework.service.file.model.FileUploadResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CkeditorFileUploadService {

  private final FileMapper fileMapper;

  private final FileService fileService;

  private static final int BUFFER_SIZE = 4096;

  @Value("${properties.fileUploadPath}")
  public String fileUploadPath;

  public void ckeditorDownload(@PathVariable int fileId, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HashMap param = new HashMap();
    param.put("fileId", fileId);

    try {
      FileUploadResponse fileInfo = fileMapper.selectFileInfo(param);
      ServletContext context = request.getServletContext();
      String fullPath = fileInfo.getFileLocation();
      String fileName = fileInfo.getFileName();
      FileInputStream inputStream = null;
      OutputStream outStream = null;

      try {
        // 경로에서 ../, ./, \.., /../ 등의 문자열을 제거
        fullPath = fullPath.replaceAll("\\.\\./", "")
            .replaceAll("\\.\\.\\\\", "")
            .replaceAll("\\./", "")
            .replaceAll("\\\\", "/");

        File baseDir = new File(fileUploadPath);
        File downloadFile = new File(fullPath);

        // 경로가 기본 디렉토리를 벗어나지 않도록 확인
        if (!downloadFile.getCanonicalPath().startsWith(baseDir.getCanonicalPath())) {
          log.info("Out of default directory path !!!!");
          return;
        }

        inputStream = new FileInputStream(downloadFile);

        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null)
          mimeType = "application/octet-stream";

        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        String headerKey = "Content-Disposition";
        String headerValue = fileService.getDisposition(fileName, request);
        response.setHeader(headerKey, headerValue);

        outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outStream.write(buffer, 0, bytesRead);
        }
      } catch (Exception e) {
        log.error("Download Error", e);
      } finally {
        try {
          if (inputStream != null)
            inputStream.close();
          if (outStream != null)
            outStream.close();
        } catch (Exception e1) {
          log.error("Download Error", e1);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      log.error("ckeditorDownload Exception !! - file_id: " + fileId);
    }
  }
}