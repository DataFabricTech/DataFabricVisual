package com.mobigen.datalake.ckeditor;

import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.service.file.FileService;
import com.mobigen.framework.service.file.model.FileUploadRequest;
import com.mobigen.framework.service.file.model.FileUploadResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@Controller
@RequiredArgsConstructor // @Value 사용시 @RequiredArgsConstructor 사용을 권장함
public class CkeditorFileUploadController {
    private final CkeditorFileUploadService commonService;
    private final FileService fileService;

    @Value("${properties.fileUploadPath}")
    public String fileUploadPath;

    /**
     * 활용 사례 파일 업로드
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ckeditor/attachFile", method = RequestMethod.POST)
    @ResponseBody
    public HashMap upload(@ModelAttribute FileUploadRequest uploadForm, HttpServletRequest request)
            throws Exception {
        JsonResult js = new JsonResult();
        uploadForm.setFiles(uploadForm.getUpload());
        HashMap obj = new HashMap();
        try {
            FileUploadResponse f = fileService.ckeditorUpload(uploadForm, fileUploadPath, request);
            obj.put("fileName", f.getOriginalFileName());
            obj.put("uploaded", 1);
            obj.put("url", "/api/ckeditor/attachFile/" + f.getFile_id());
        } catch (Exception e) {
        }
        return obj;
    }

    /**
     * 활용 사례 파일 다운로드
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ckeditor/attachFile/{fileId}", method = RequestMethod.GET)
    public void ckeditorDownload(@PathVariable int fileId, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        commonService.ckeditorDownload(fileId, request, response);
    }

}