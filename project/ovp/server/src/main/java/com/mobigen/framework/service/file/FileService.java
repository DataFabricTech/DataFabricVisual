package com.mobigen.framework.service.file;

import com.mobigen.framework.service.file.model.FileUploadRequest;
import com.mobigen.framework.service.file.model.FileUploadResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {
	private static final Random RANDOM = new Random();
	private final MessageSource messageSource;

	private String getRandomFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int seed = RANDOM.nextInt(1000) + 8999;
		return sdf.format(new Date()) + seed;
	}

	private String getPath(String type) {
		String basePath = messageSource.getMessage("file.directory", null, Locale.getDefault());
		String subPathKey = "file." + type;
		String subPath = messageSource.getMessage(subPathKey, null, Locale.getDefault());
		return String.format("%s/%s", basePath, subPath);
	}

	private String getUploadPath(String path, String fileName) {
		File directory = new File(path);
		if (!directory.exists()){
			directory.mkdirs();
		}
		return String.format("%s/%s", path, fileName);
	}

    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.contains("MSIE")) {
            return "MSIE";
        } else if (header.contains("Chrome")) {
            return "Chrome";
        } else if (header.contains("Opera")) {
            return "Opera";
        }
        return "Firefox";
    }

    /**
     * 브라우저별 다운로드 해더값 생성
     * @param filename
     * @param request
     * @return
	 */
    public String getDisposition(String filename, HttpServletRequest request) {
    	String browser = getBrowser(request);

        String dispositionPrefix = "attachment;filename=";
        String encodedFilename;

		switch (browser) {
			case "MSIE" ->
					encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
			case "Firefox", "Opera" ->
					encodedFilename = "\"" + new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"";
			case "Chrome" -> {
				StringBuilder sb = new StringBuilder();
				for (char c : filename.toCharArray()) {
					if (c > '~') {
						sb.append(URLEncoder.encode(String.valueOf(c), StandardCharsets.UTF_8));
					} else {
						sb.append(c);
					}
				}
				encodedFilename = sb.toString();
			}
			default ->
				encodedFilename = "";
		}

        return dispositionPrefix + encodedFilename;
    }

	/**
	 * 다운로드 경로 생성
	 * @param type
	 * @param fileName
	 * @return
	 */
	public String getDownloadPath(String type, String fileName) {
		return String.format("%s/%s", getPath(type), fileName);
	}

	/**
	 * 파일 업로드
	 * @param uploadForm
	 * @return
	 * @throws Exception
	 */
	public List<FileUploadResponse> fileUpload(FileUploadRequest uploadForm, String savePath) throws Exception {
		List<FileUploadResponse> result = new ArrayList<>();

        List<MultipartFile> files = uploadForm.getFiles();
        if(null != files && !files.isEmpty()) {
            for (MultipartFile multipartFile : files) {
                String originalFileName = multipartFile.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());

                String saveFileName = getRandomFileName() + extension;
				String path = getUploadPath(savePath, saveFileName);
				log.info("File Upload Path: " + path);

				multipartFile.transferTo(new File(path));
				FileUploadResponse fure = new FileUploadResponse(originalFileName, saveFileName, uploadForm.getType());
				result.add(fure);
            }
        }
		return result;
	}
}
