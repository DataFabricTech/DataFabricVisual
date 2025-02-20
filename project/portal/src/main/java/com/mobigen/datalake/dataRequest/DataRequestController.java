package com.mobigen.datalake.dataRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.service.file.FileService;
import com.mobigen.framework.service.file.model.FileUploadRequest;
import com.mobigen.framework.service.file.model.FileUploadResponse;
import com.mobigen.framework.utility.RegexUtils;

import org.springframework.core.env.Environment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping("/api/dataRequest")
@Controller
@AllArgsConstructor
public class DataRequestController {
    private final Environment env;
    private final FileService fileService;
    private final DataRequestService dataRequestService;
    private final SessionManager sessionManager;

    /**
     * 데이터요청 목록 조회
     * @param pageSize                  페이지 크기
     * @param pnum                      페이지 번호
     * @param searchType                검색 종류
     * @param searchWord                검색어
     * @return                          게시글 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getDataRequestList(@RequestParam("pageSize") int pageSize, 
                                     @RequestParam("pnum") int pnum,
                                     @RequestParam("searchType") String searchType,
                                     @RequestParam("searchWord") String searchWord,
                                     @RequestParam("searchStatus") String searchStatus) throws Exception {

        Map<String, String> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("searchStatus", searchStatus);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));

        int total = dataRequestService.getDataRequestListTotal(param);
        
        List<Map> list = dataRequestService.getDataRequestList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    /**
     * 데이터요청 단건 조회
     * @param id                    게시글 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/one")
    public Object getDataRequestOne(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
        return dataRequestService.getDataRequestOne(id, request);
    }

    /**
     * 데이터요청 저장 : 파일저장 포함
     * @param uploadForm                파일
     * @param request
     * @return
     */
    @RequestMapping(value = "/add/saveFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult insertFile(@ModelAttribute("uploadForm") FileUploadRequest uploadForm, HttpServletRequest request)  {
        JsonResult js = new JsonResult();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataRequest = objectMapper.convertValue(uploadForm.getDataRequest(), Map.class);

            // 세션의 사용자 ID를 작성자 명으로 등록
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);

            if (user != null) {
                dataRequest.put("writer", user.get("user_id"));
                Integer id = (Integer) dataRequestService.createDataRequest(dataRequest, request);
                uploadForm.setPost_id((Integer) dataRequest.get("id"));

                // 파일 업로드 경로 숨기기
                List<FileUploadResponse> uploadResult = fileService.fileUpload(uploadForm, env.getProperty("properties.fileUploadPath"), request);
                if (uploadResult.size() != 0)
                    uploadResult.get(0).setSaveFileName(null);
                    
                js.setData(uploadResult);
            } else {
                js.setData(0);
                js.setErrorMessage("데이터 요청 저장에 실패하였습니다.");
            }

        } catch (Exception e) {
            js.setErrorMessage(e.getMessage());
            js.setErrorMessage("파일 업데이트에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }

        return js;
    }

    /**
     * 데이터요청 삭제
     * @param param                 게시글 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/delete")
    @ResponseBody
    public Object deleteDataRequest(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        JsonResult js = new JsonResult();

        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        String userRole = user.get("authority").toString();

        List paramDeleteList = ((List)param.get("deleteList"));

        // 세션의 사용자가 관리자가 아닌 경우 사용자 검증
        if ("Super-Admin".equals(userRole) || "Admin".equals(userRole)) {
            js.setData(dataRequestService.deleteDataRequest(param));
        } else {
            Boolean isValid = false;
            for (int i=0; i<paramDeleteList.size(); i++){
                Map<String, Object> dataRequestOne = dataRequestService.getDataRequestOne(paramDeleteList.get(i).toString(), request);

                // 게시글이 없으면 다음 확인
                if (dataRequestOne.size() == 0) {
                    continue;
                }

                // 세션 사용자 ID와 작성자 비교
                String writer = ((Map<String, Object>) dataRequestOne.get("dataRequest")).get("writer").toString();;

                // 다른 경우 삭제처리 하지 않음
                if (!writer.equals(userId)) {
                    isValid = false;
                    break;
                } else {
                    isValid = true;    
                }
            }
            
            // 검증 성공한 경우만 삭제 처리
            if (isValid) {
                Map<String, Object> result = (Map<String, Object>) dataRequestService.deleteDataRequest(param);
                Integer deleteCnt = result.get("deleteCnt") != null ? Integer.parseInt(result.get("deleteCnt").toString()) : 0;
                js.setData(deleteCnt);
            } else {
                js.setData(0);
            }
        }
       
        return js;
    }

    /**
     * 데이터요청 수정
     * @param param                 게시글 수정 내용
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/update")
    @ResponseBody
    public Object updatePost(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Object result = 0;
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        String userRole = user.get("authority").toString();
        
        if ("Super-Admin".equals(userRole) || "Admin".equals(userRole)) {
            param.put("writer", userId); 
            result = dataRequestService.updateDataRequest(param, null);
        } else {
            if (userId != null && !userId.equals("")) {
                if (userId.equals(param.get("writer").toString())) {
                    param.put("writer", userId); 
                    result = dataRequestService.updateDataRequest(param, null);
                }
            }
        }

        return result;
    }

    /**
     * 데이터요청 수정 : 파일저장 포함
     * @param uploadForm             파일
     * @param request
     * @return
     */
    @RequestMapping(value = "/update/saveFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updateDataRequestFile(@ModelAttribute("uploadForm") FileUploadRequest uploadForm, HttpServletRequest request) throws Exception {
        JsonResult js = new JsonResult();

        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        String userRole = user.get("authority").toString();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = objectMapper.convertValue(uploadForm.getDataRequest(), Map.class);

            // request_end, request_start 파라미터 검증
            if (params.get("request_end") != null && !params.get("request_end").toString().equals("")) {
                // 앞에 2024-11-05 부분만 저장
                String request_end = params.get("request_end").toString().substring(0, 10);
                RegexUtils.sanitizeInput(request_end);
            }

            if (params.get("request_start") != null && !params.get("request_start").toString().equals("")) {
                // 앞에 2024-11-05 부분만 저장
                String request_start = params.get("request_start").toString().substring(0, 10);
                RegexUtils.sanitizeInput(request_start);
            }

            List<String> delete_files = uploadForm.getDelete_files();
            Boolean delete_thumb_flag = uploadForm.getDelete_thumb_flag();

            // 세션의 사용자가 관리자가 아닌 경우 사용자 검증
            if ("Super-Admin".equals(userRole) || "Admin".equals(userRole)) {
                dataRequestService.updateDataRequest(params, delete_files);

                if(delete_thumb_flag != null && delete_thumb_flag)
                    dataRequestService.deleteThumb(params);

                uploadForm.setPost_id((Integer) params.get("id"));
                // 파일 업로드 경로 숨기기
                List<FileUploadResponse> uploadResult = fileService.fileUpload(uploadForm, env.getProperty("properties.fileUploadPath"), request);
                if(uploadResult.size() != 0)
                    uploadResult.get(0).setSaveFileName(null);

                js.setData(uploadResult);

            } else {
                // 세션의 사용자 ID와 작성자가 동일한 경우만 수정 처리
                if (userId != null && !userId.equals("")) {
                    Map<String, Object> selectedPost = dataRequestService.getDataRequestOne(params.get("id").toString(), request);
                    String writer = (((Map<String, Object>)selectedPost.get("post")).get("writer")).toString();
                    if (userId.equals(writer)) {
                        dataRequestService.updateDataRequest(params, delete_files);

                        if(delete_thumb_flag != null && delete_thumb_flag)
                            dataRequestService.deleteThumb(params);

                        uploadForm.setPost_id((Integer) params.get("id"));
                        // 파일 업로드 경로 숨기기
                        List<FileUploadResponse> uploadResult = fileService.fileUpload(uploadForm, env.getProperty("properties.fileUploadPath"), request);
                        if(uploadResult.size() != 0)
                            uploadResult.get(0).setSaveFileName(null);

                        js.setData(uploadResult);
                    } else {
                        js.setData(0);
                    }
                } else {
                    js.setData(0);
                }
            }
            

        } catch (Exception e) {
            js.setErrorMessage("파일 업데이트에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }

        return js;
    }

    /**
     * 데이터요청 답변 저장
     * @param param                 답변 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/updateAdminReply")
    @ResponseBody
    public Object updateAdminReply(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        return dataRequestService.updateAdminReply(param, request);
    }

    /**
     * 데이터 요청 답변 삭제
     * @param param                 데이터 요청 답변 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteAdminReply")
    @ResponseBody
    public Object deleteAdminReply(@RequestBody Map<String, Object> param) throws Exception {
        return dataRequestService.deleteAdminReply(param);
    }
}
