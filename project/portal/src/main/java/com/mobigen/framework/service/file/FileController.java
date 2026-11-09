package com.mobigen.framework.service.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.service.file.model.FileUploadRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping(value = "/api/service/file")
@RequiredArgsConstructor
public class FileController {

	private static final Logger logger = LoggerFactory
			.getLogger(FileController.class);

	private static final int BUFFER_SIZE = 4096;

	private final FileService fileService;
	private final SessionManager sessionManager;

	@Value("${properties.fileUploadPath}")
	public String fileUploadPath;

	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult upload(@ModelAttribute("uploadForm") FileUploadRequest uploadForm, HttpServletRequest request) {
		JsonResult js = new JsonResult();

		try {
			js.setData(fileService.fileUpload(uploadForm, fileUploadPath, request));
		} catch (Exception e) {
			logger.error("Error", e);
		}

		return js;
	}

	@GetMapping("/fileDownload")
	public void download(@RequestParam("fileId") int fileId,
						 HttpServletRequest request,
						 HttpServletResponse response) throws Exception {

		ServletContext context = request.getServletContext();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("file_id", fileId);
		Map<String, Object> filePath = fileService.getDownloadPath(param);
		String fullPath = (String) filePath.get("file_location");
		String originalFileName = (String) filePath.get("file_name");

		FileInputStream inputStream = null;
		OutputStream outStream = null;
		

		try {
			// 데이터 요청의 경우 본인 게시글의 파일인지 검증
			Map<String, Object> user = sessionManager.getUserByAccessToken(request);
			if (user != null) {
				// 본인 게시글의 파일이 아닌 경우 오류
				Map<String, Object> fileInfo = fileService.getDataRequestFileInfo(fileId);
				// 데이터 요청 게시글로 조회됨
				if (fileInfo != null) {
					String reg_user = fileInfo.get("reg_user").toString();
					// 데이터 요청 게시글에서 파일을 올린 사람이 본인이 아닌 경우 다운로드 불가
					if (!reg_user.equals(user.get("user_id"))) {

						// 관리자의 경우 예외처리
						String role_name = user.get("authority").toString();
						if (!role_name.equals("Super-Admin")) {
							throw new SecurityException("본인이 작성한 첨부파일만 다운로드 가능합니다.");
						}

					} else {
						log.info("본인이 작성한 파일 다운로드 시도");
					}
				}
			} else {
				throw new Exception("파일 다운로드에 실패하였습니다.");
			}

		// 경로에서 ../, ./, \.., /../ 등의 문자열을 제거
			fullPath = fullPath.replaceAll("\\.\\./", "")
													.replaceAll("\\.\\.\\\\", "")
													.replaceAll("\\./", "")
													.replaceAll("\\\\", "/");

			File baseDir = new File(fileUploadPath);
			File downloadFile = new File(fullPath);

			// 경로가 기본 디렉토리를 벗어나지 않도록 확인
			if (!downloadFile.getCanonicalPath().startsWith(baseDir.getCanonicalPath())) {
				throw new SecurityException("허가되지 않은 파일 접근 시도.");
			}
			
			inputStream = new FileInputStream(downloadFile);

			String mimeType = context.getMimeType(fullPath);
			if (mimeType == null)
				mimeType = "application/octet-stream";
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = fileService.getDisposition(originalFileName, request);
			response.setHeader(headerKey, headerValue);
			outStream = response.getOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			logger.error("Download Error", e);
			throw new Exception("파일 다운로드에 실패하였습니다.");
		} finally {
			try {
				if (inputStream != null) inputStream.close();
				if (outStream != null) outStream.close();
			} catch (Exception e1) {
				logger.error("Download Error", e1);
				throw new Exception("파일 다운로드에 실패하였습니다.");
			}
		}
	}

	@GetMapping("/list")
	@ResponseJsonResult
	public Object getFileList()throws Exception{
		return fileService.getFileList();
	}

	@PostMapping("/delete/sample")
	@ResponseBody
    public void deleteSample(@RequestBody Map<String, String> request) throws Exception {
		String fileId= request.get("fileId");
        fileService.deleteSample(fileId);
    }

}

