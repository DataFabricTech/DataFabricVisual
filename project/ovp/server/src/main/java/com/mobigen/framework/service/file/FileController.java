package com.mobigen.framework.service.file;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.service.file.model.FileUploadRequest;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@RestController
@RequestMapping(value = "/service/file")
@RequiredArgsConstructor
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	private static final int BUFFER_SIZE = 4096;

	private final FileService fileService;

	@PostMapping(value = "/upload")
	public JsonResult upload(@ModelAttribute("uploadForm") FileUploadRequest uploadForm, HttpServletRequest request) {
		JsonResult js = new JsonResult();
		try {
			File workDir = (File) request.getServletContext().getAttribute(ServletContext.TEMPDIR);
			String uploadPath = workDir.getPath() + "/file-upload-test";
			js.setData(fileService.fileUpload(uploadForm, uploadPath));
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return js;
	}

	@GetMapping(value = "/download")
	public void download(@RequestParam("originalFileName") String originalFileName,
			@RequestParam("saveFileName") String saveFileName,
			@RequestParam("type") String type,
			HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = request.getServletContext();
		String fullPath = fileService.getDownloadPath(type, saveFileName);

		File downloadFile = new File(fullPath);
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		String headerKey = "Content-Disposition";
		String headerValue = fileService.getDisposition(originalFileName, request);
		response.setHeader(headerKey, headerValue);

		try(
				FileInputStream inputStream = new FileInputStream(downloadFile);
				OutputStream outStream = response.getOutputStream();
		) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			logger.error("Download Error", e);
		}
	}
}
