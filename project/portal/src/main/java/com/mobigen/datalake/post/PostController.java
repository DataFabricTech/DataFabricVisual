package com.mobigen.datalake.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobigen.framework.iris.User;
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
import java.util.regex.Pattern;

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
@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class PostController {
    private final Environment env;
    private final FileService fileService;
    private final PostService postService;
    private final SessionManager sessionManager;

    @ResponseJsonResult
    @GetMapping("/post")
    public Object getAllPost(@RequestParam("pnum") String pnum, @RequestParam("type") String type, @RequestParam("word") String word, @RequestParam("postId") String postId) throws Exception {
        PageHelper.startPage(Integer.parseInt(pnum), 2);
        return PageInfo.of(postService.getPost(type, word, postId));
    }

    /**
     * FAQ 전체 조회
     * @param postType                  게시글 종류
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/post-all")
    public Object getAllPost(@RequestParam("postType") String postType) throws Exception {
        return postService.getAllPost(postType);
    }

    /**
     * 마이 데이터 요청 내역 조회
     * @param pageSize              페이지 크기
     * @param pnum                  페이지 번호
     * @param postType              게시글 종류
     * @param searchType            검색 종류
     * @param searchWord            검색어
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/post/request/list")
    public Object getMyRequestList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum,
                                   @RequestParam("postType") String postType,
                                   @RequestParam("searchType") String searchType,
                                   @RequestParam("searchWord") String searchWord,
                                   HttpServletRequest request) throws Exception {


        Map<String, String> param = new HashMap<>();
        param.put("postType", postType);
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        param.put("user_id", user.get("user_id").toString());

        int total = postService.getPostListTotal(param);
        
        List<Map> list = postService.getPostList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    /**
     * 공지사항, 데이터요청, FAQ 목록 조회
     * @param pageSize                  페이지 크기
     * @param pnum                      페이지 번호
     * @param postType                  게시글 종류
     * @param searchType                검색 종류
     * @param searchWord                검색어
     * @return                          게시글 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/post/list")
    public Object getPostList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum,
                              @RequestParam("postType") String postType,
                              @RequestParam("searchType") String searchType,
                              @RequestParam("searchWord") String searchWord) throws Exception {

        Map<String, String> param = new HashMap<>();
        param.put("postType", postType);
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));

        int total = postService.getPostListTotal(param);
        
        List<Map> list = postService.getPostList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    /**
     * 공지사항, 데이터요청, FAQ 단건 조회
     * @param postId                    게시글 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/post/one")
    public Object getPostOne(@RequestParam("postId") String postId, HttpServletRequest request) throws Exception {
        return postService.getPostOne(postId, request);
    }

    /**
     * 공지사항, 데이터요청, FAQ 저장
     * @param param                     게시글 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/post/add")
    @ResponseBody
    public Object createPost(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Object result = 0;
        // 세션의 사용자 ID를 작성자 명으로 등록
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        if (userId != null && !userId.equals("")) {
            param.put("writer", userId);
            result = postService.createPost(param, request);
        }

        return result;
    }

    /**
     * 공지사항, 데이터요청, FAQ 삭제
     * @param param                 게시글 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/post/delete")
    @ResponseBody
    public Object deletePost(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        JsonResult js = new JsonResult();

        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        String userRole = user.get("authority").toString();

        List paramDeleteList = ((List)param.get("deleteList"));

        // 세션의 사용자가 관리자가 아닌 경우 사용자 검증
        if ("Super-Admin".equals(userRole) || "Admin".equals(userRole)) {
            js.setData(postService.deletePost(param));
        } else {
            Boolean isValid = false;
            for (int i=0; i<paramDeleteList.size(); i++){
                Map<String, Object> postOne = postService.getPostOne(paramDeleteList.get(i).toString(), request);

                // 게시글이 없으면 다음 확인
                if (postOne.size() == 0) {
                    continue;
                }

                // 세션 사용자 ID와 작성자 비교
                String writer = ((Map<String, Object>) postOne.get("post")).get("writer").toString();;

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
                js.setData(Integer.parseInt(postService.deletePost(param).toString()));
            } else {
                js.setData(0);
            }
        }
       
        return js;
    }

    /**
     * 공지사항, 데이터요청 저장 : 파일저장포함
     * @param uploadForm                파일
     * @param request
     * @return
     */
    @RequestMapping(value = "/post/add/saveFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult insertFile(@ModelAttribute("uploadForm") FileUploadRequest uploadForm, HttpServletRequest request)  {
        System.out.println("insertFile");
        JsonResult js = new JsonResult();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> post = objectMapper.convertValue(uploadForm.getPost(), Map.class);

            // 세션의 사용자 ID를 작성자 명으로 등록
            Map<String, Object> user = sessionManager.getUserByAccessToken(request);

            if (user != null) {
                post.put("writer", user.get("user_id"));
                Integer id = (Integer) postService.createPost(post, request);
                uploadForm.setPost_id((Integer) post.get("id"));

                // 파일 업로드 경로 숨기기
                List<FileUploadResponse> uploadResult = fileService.fileUpload(uploadForm, env.getProperty("properties.fileUploadPath"), request);
                if (uploadResult.size() != 0)
                    uploadResult.get(0).setSaveFileName(null);
                    
                js.setData(uploadResult);
            } else {
                js.setData(0);
            }

        } catch (Exception e) {
            js.setErrorMessage("파일 업데이트에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }

        return js;
    }

    /**
     * 공지사항, 데이터요청 수정 : 파일저장포함
     * @param uploadForm             파일
     * @param request
     * @return
     */
    @RequestMapping(value = "/post/update/saveFile", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult updatePostFile(@ModelAttribute("uploadForm") FileUploadRequest uploadForm, HttpServletRequest request) throws Exception {
        JsonResult js = new JsonResult();

        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        String userRole = user.get("authority").toString();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = objectMapper.convertValue(uploadForm.getPost(), Map.class);

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
                postService.updatePost(params, delete_files);

                if(delete_thumb_flag != null && delete_thumb_flag)
                  postService.deleteThumb(params);

                uploadForm.setPost_id((Integer) params.get("id"));
                // 파일 업로드 경로 숨기기
                List<FileUploadResponse> uploadResult = fileService.fileUpload(uploadForm, env.getProperty("properties.fileUploadPath"), request);
                if(uploadResult.size() != 0)
                    uploadResult.get(0).setSaveFileName(null);

                js.setData(uploadResult);

            } else {
                // 세션의 사용자 ID와 작성자가 동일한 경우만 수정 처리
                if (userId != null && !userId.equals("")) {
                    Map<String, Object> selectedPost = postService.getPostOne(params.get("id").toString(), request);
                    String writer = (((Map<String, Object>)selectedPost.get("post")).get("writer")).toString();
                    if (userId.equals(writer)) {
                        postService.updatePost(params, delete_files);

                        if(delete_thumb_flag != null && delete_thumb_flag)
                            postService.deleteThumb(params);

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
     * 공지사항, 데이터요청, FAQ 수정
     * @param param                 게시글 수정 내용
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/post/update")
    @ResponseBody
    public Object updatePost(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        Object result = 0;
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        String userRole = user.get("authority").toString();
        
        if ("Super-Admin".equals(userRole) || "Admin".equals(userRole)) {
            param.put("writer", userId); 
            result = postService.updatePost(param, null);
        } else {
            if (userId != null && !userId.equals("")) {
                if (userId.equals(param.get("writer").toString())) {
                    param.put("writer", userId); 
                    result = postService.updatePost(param, null);
                }
            }
        }

        return result;
    }

    /**
     * FAQ 순서 저장
     * @param param                 FAQ 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/post/updatePostFaqOrdNo")
    @ResponseBody
    public Object updatePostFaqOrdNo(@RequestBody Map<String, Object> param) throws Exception {
        return postService.updatePostFaqOrdNo(param);
    }

    /**
     * 데이터요청 답변 저장
     * @param param                 답변 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/post/updateAdminReply")
    @ResponseBody
    public Object updateAdminReply(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        return postService.updateAdminReply(param, request);
    }

    /**
     * 데이터 요청 답변 삭제
     * @param param                 데이터 요청 답변 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/post/deleteAdminReply")
    @ResponseBody
    public Object deleteAdminReply(@RequestBody Map<String, Object> param) throws Exception {
        return postService.deleteAdminReply(param);
    }
}
