package com.mobigen.framework.service.file;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.security.SecureRandom;

import jakarta.servlet.http.HttpServletRequest;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.framework.iris.User;
import com.mobigen.framework.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mobigen.framework.service.file.model.FileUploadRequest;
import com.mobigen.framework.service.file.model.FileUploadResponse;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class FileService {
	private final SessionManager sessionManager;

	private final FileMapper fileMapper;

	@Value("#{'${properties.allowedExtension}'.split(',')}")
	private List<String> extList;

	@Value("#{'${properties.allowedImgExtension}'.split(',')}")
	private List<String> extImgList;

	private String getRandomFileName()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		// SecureRandom을 사용해 예측 불가능한 무작위성을 확보
		SecureRandom secureRandom = new SecureRandom();
		int seed = secureRandom.nextInt(1000) + 8999; // 1000에서 9999 사이의 무작위 값

		String result = sdf.format(new Date()) + seed + "";
		return result;
	}

	private String getUploadPath(String path, String fileName)
	{
		File fDir = new File(path);
		if (!fDir.exists())
			fDir.mkdirs();

		path += "/" + fileName;

		return path;
	}

	private String getBrowser(HttpServletRequest request)
	{
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	/**
	 * 브라우저별 다운로드 해더값 생성
	 * @param filename
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String getDisposition(String filename, HttpServletRequest request) throws Exception
	{
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment;filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE"))
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");

		else if (browser.equals("Firefox"))
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";

		else if (browser.equals("Opera"))
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";

		else if (browser.equals("Chrome"))
		{
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		}

		return dispositionPrefix + encodedFilename;
	}

	public FileUploadResponse ckeditorUpload(FileUploadRequest uploadForm, String savePath, HttpServletRequest request) throws Exception {
		List<FileUploadResponse> list = fileUpload(uploadForm, savePath, request);
		return list.get(0);
	}

	public Map<String, Object> getDownloadPath(Map<String, Object> param) throws Exception {
		Map<String, Object> result = fileMapper.getFileFullPath(param);
//		result.forEach((key, value)
//				-> System.out.println("key: " + key + ", value: " + value));
		return result;
	}

//	public String getOriginalfileName(String fileNum) {
//		Map<String, Object> result = fileMapper.getOriginalFileName(fileNum);
//
//		return (String) result.get("fileName");
//	}
	private boolean validateUploadFile(List<MultipartFile> files) {
		boolean isValidate = false;

		for (MultipartFile multipartFile : files)
		{
			String originalFileName = multipartFile.getOriginalFilename();
			String extType = originalFileName.substring(originalFileName.indexOf("."));

			// 확장자 확인
			for (String ext : extList) {
				if(extType.toLowerCase().endsWith("." + ext.trim())) {
					isValidate = true;
				}
			}
		}
		return isValidate;
	}

	private boolean validateUploadHiveConfigFile(List<MultipartFile> files) {
		boolean isValidate = false;

		for (MultipartFile multipartFile : files)
		{
			String originalFileName = multipartFile.getOriginalFilename();
			String extType = originalFileName.substring(originalFileName.indexOf("."));

			// 확장자 확인
			if(extType.toLowerCase().endsWith(".xml")) {
				isValidate = true;
			}
			
		}
		return isValidate;
	}

	public Map<String, Object> getDataRequestFileInfo(int user_id) throws Exception {
		return fileMapper.getDataRequestFileInfo(user_id);
	}

	private boolean validateThumbFile(List<MultipartFile> files) {
		boolean isValidate = false;

		for (MultipartFile multipartFile : files)
		{
			String originalFileName = multipartFile.getOriginalFilename();
			String extType = originalFileName.substring(originalFileName.indexOf("."));

			// 확장자 확인
			for (String ext : extImgList) {
				if(extType.toLowerCase().endsWith("." + ext.trim())) {
					isValidate = true;
				}
			}
		}
		return isValidate;
	}

	/**
     * 파일 업로드
     * @param uploadForm
     * @return
     * @throws Exception
     */
    public List<FileUploadResponse> fileUpload(FileUploadRequest uploadForm, String savePath, HttpServletRequest request) throws Exception
    {
        List<FileUploadResponse> result = new ArrayList<FileUploadResponse>();
        SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
        // 파일 저장 경로에 월별 폴더 추가
        String fullDirPath = savePath + "/" + ym.format(new Date());

        // --- 파일 업로드 로직 시작 ---
        List<MultipartFile> files = uploadForm.getFiles();
        if(null != files && files.size() > 0)
        {
            // 확장자 확인
            if(!validateUploadFile(files)) {
                throw new Exception("확장자가 잘못되었습니다.");
            }

            // ⚠️ 디렉토리 생성 로직: 파일을 저장하기 전에 한 번만 수행
            File dir = new File(fullDirPath);
            if (!dir.exists()) {
                // dir.mkdirs()를 호출하여 상위 경로가 모두 없어도 생성합니다.
                // 이 메소드는 생성에 실패하면 false를 반환하므로, 로그를 남기거나 예외를 던지는 것이 좋습니다.
                if (!dir.mkdirs()) {
                    throw new IOException("파일 저장 디렉토리(" + fullDirPath + ") 생성에 실패했거나 쓰기 권한이 없습니다.");
                }
            }

            for (MultipartFile multipartFile : files)
            {
                String originalFileName = multipartFile.getOriginalFilename();
                String extType = originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());

                String saveFileName = getRandomFileName() + extType;
                // getUploadPath 함수가 fullDirPath와 saveFileName을 결합하여 절대 경로를 반환해야 합니다.
                String fullFilePath = getUploadPath(fullDirPath, saveFileName); 
                
                File f = new File(fullFilePath);
                
                // 파일 저장 시도. dir.mkdirs() 성공 후 권한 문제 또는 path 문제로 여기서 오류 발생 가능성이 높습니다.
                multipartFile.transferTo(f);

                String fullFileName = fullDirPath + "/" + saveFileName;
                String loginId = "";
                Map<String, Object> user = sessionManager.getUserByAccessToken(request);
                if(user != null) {
                    loginId = user.get("user_id").toString();
                }

                int file_id =fileMapper.fineFileId()+1;

                FileUploadResponse fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartFile.getSize(),file_id);
                if (file_id == 1) {
                    fileMapper.insertFile(fure);
                    file_id =fileMapper.fineFileId()+1;
                    fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartFile.getSize(),file_id);
                }
                fure.setPost_id(uploadForm.getPost_id());
                fure.setFile_type("file");
                fileMapper.insertFile(fure);
                
                result.add(fure);
            }
        }

        // --- 썸네일 업로드 로직 시작 ---
        List<MultipartFile> thumb_img = uploadForm.getThumb_img();
        if(null != thumb_img && thumb_img.size() > 0)
        {
            // 확장자 확인
            if(!validateThumbFile(thumb_img)) {
                throw new Exception("확장자가 잘못되었습니다.");
            }
            
            // ⚠️ 디렉토리 생성 로직: 썸네일 업로드 직전에 다시 한번 확인 (상위 블록에서 이미 했지만 안전을 위해)
            File dir = new File(fullDirPath);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new IOException("파일 저장 디렉토리(" + fullDirPath + ") 생성에 실패했거나 쓰기 권한이 없습니다.");
                }
            }

            for (MultipartFile multipartThumbFile : thumb_img)
            {
                String originalFileName = multipartThumbFile.getOriginalFilename();
                String extType = originalFileName.substring(originalFileName.indexOf("."), originalFileName.length());

                String saveFileName = getRandomFileName() + extType;
                String fullFilePath = getUploadPath(fullDirPath, saveFileName);

                File f = new File(fullFilePath);
                multipartThumbFile.transferTo(f);

                String fullFileName = fullDirPath + "/" + saveFileName;
                String loginId = "";
                Map<String, Object> user = sessionManager.getUserByAccessToken(request);
                if(user != null) {
                    loginId = user.get("user_id").toString();
                }

                int file_id =fileMapper.fineFileId()+1;

                FileUploadResponse fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartThumbFile.getSize(),file_id);
                if (file_id == 1) {
                    fileMapper.insertFile(fure);
                    file_id =fileMapper.fineFileId()+1;
                    fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartThumbFile.getSize(),file_id);
                }
                fure.setPost_id(uploadForm.getPost_id());
                fure.setFile_type("thumb");
                fileMapper.insertFile(fure);

                result.add(fure);
            }
        }

        return result;
    }

	/**
	 * 샘플파일 업로드
	 * @param uploadForm
	 * @return
	 * @throws Exception
	 */
	public List<FileUploadResponse> fileSampleUpload(FileUploadRequest uploadForm, String savePath) throws Exception
	{
		List<FileUploadResponse> result = new ArrayList<FileUploadResponse>();

		List<MultipartFile> files = uploadForm.getFiles();
		if(null != files && files.size() > 0)
		{
			// 확장자 확인
			if(!validateUploadFile(files)) {
				throw new Exception("확장자가 잘못되었습니다.");
			}

			for (MultipartFile multipartFile : files)
			{
				String originalFileName = multipartFile.getOriginalFilename();
				String extType = originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());

				String saveFileName = "sampleFile"+ extType;
				String path = getUploadPath(savePath, saveFileName);

				File f = new File(path);
				multipartFile.transferTo(f);

				String fullFileName = savePath + "/" + saveFileName;
				String loginId = "";
				if(sessionManager != null || sessionManager.getUser() != null ) {
					loginId = sessionManager.getUser().getUserId();
				}

				int file_id =fileMapper.fineFileId()+1;

				FileUploadResponse fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartFile.getSize(),file_id);
				if (file_id == 1) {
					fileMapper.insertFile(fure);
					file_id =fileMapper.fineFileId()+1;
					fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartFile.getSize(),file_id);
				}
				fure.setPost_id(0);
				fileMapper.insertFile(fure);
				
				result.add(fure);}

		}
		return result;
	}

	/**
	 * Hive 설정파일 업로드
	 * @param uploadForm
	 * @return
	 * @throws Exception
	 */
	public List<FileUploadResponse> fileHiveConfigUpload(FileUploadRequest uploadForm, String savePath, HttpServletRequest request) throws Exception {
		List<FileUploadResponse> result = new ArrayList<FileUploadResponse>();
		String system_id = uploadForm.getSystemId();

		MultipartFile coreSiteFile = uploadForm.getCoreSiteFile();
		MultipartFile hdfsSiteFile = uploadForm.getHdfsSiteFile();
		List<MultipartFile> files = new ArrayList<>();
		if (coreSiteFile != null) files.add(coreSiteFile);
		if (hdfsSiteFile != null) files.add(hdfsSiteFile);

		if(null != files && files.size() > 0) {
			// 확장자 확인
			if(!validateUploadHiveConfigFile(files)) {
				throw new Exception("확장자가 잘못되었습니다.");
			}

			for (MultipartFile multipartFile : files) {
				String originalFileName = multipartFile.getOriginalFilename();

				String extType = originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());
				
				String newSavePath = savePath + "/" + system_id;
				String saveFileName = originalFileName;
				String path = getUploadPath(newSavePath, saveFileName);

				File f = new File(path);
				multipartFile.transferTo(f);

				String fullFileName = newSavePath + "/" + saveFileName;
				String loginId = "";
				Map<String, Object> user = sessionManager.getUserByAccessToken(request);
				if(user != null) {
					loginId = user.get("user_id").toString();
				}

				int file_id =fileMapper.fineFileId() + 1;

				FileUploadResponse fure = new FileUploadResponse(originalFileName, fullFileName, uploadForm.getType(), loginId, multipartFile.getSize(),file_id);
				
				// system_id를 post_id에 설정
				fure.setPost_id(null);
				fure.setFile_type("config");

				// 기존에 경로가 있으면 DB reg_date만 업데이트, 신규추가하지 않음
				Map<String, Object> param = new HashMap<>();
				param.put("system_id", system_id);
				param.put("file_name", saveFileName);
				Integer dupCnt = fileMapper.getFileDupCnt(param);
				if (dupCnt != null && dupCnt > 0) 
					fileMapper.updateFileRegDate(param);
				else 
					fileMapper.insertFile(fure); 
				
				result.add(fure);
			}
		}
		return result;
	}


	public List<Map> getFileList()throws Exception{
		List<Map> fileList = (List<Map>) fileMapper.getFileList("0", "file");
		return fileList;
	}

	public void deleteSample(String file_id) throws Exception {
		fileMapper.deleteFile(file_id);
	}
}
