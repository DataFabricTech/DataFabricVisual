package com.mobigen.datalake.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final FileMapper fileMapper;
    private final SessionManager sessionManager;
    
    public List<Map> getPost(String type, String word, String postId) throws Exception {
        List<Map> post = (List<Map>) postMapper.getPost(type, word, postId);
        return post;
    }

    public List<Map> getAllPostID() throws Exception {
        List<Map> postID = (List<Map>) postMapper.getAllPostID();
        return postID;
    }

    // FAQ 전체 조회
    public List<Map> getAllPost(String postType) throws Exception {
        List<Map> postList = (List<Map>) postMapper.getAllPost(postType);
        return postList;
    }

    // 공지사항, FAQ 목록 전체개수 조회
    public int getPostListTotal(Map<String, String> param) throws Exception {
        int total = postMapper.getPostListTotal(param);
        return total;
    }
    
    // 공지사항, FAQ 목록 조회
    public List<Map> getPostList(Map<String, String> param) throws Exception {
        List<Map> post = (List<Map>) postMapper.getPostList(param);
        return post;
    }

    // 공지사항, FAQ 단건 조회
    public Map<String, Object> getPostOne(String postId, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        postMapper.updateViewCount(postId);
        Map<String, Object> post = postMapper.getPostOne(postId);

        Map<String, Object> user = sessionManager.getUserByAccessToken(request);

        List<Map> fileList = (List<Map>) fileMapper.getFileList(postId, "file");
        List<Map> thumbList = (List<Map>) fileMapper.getFileList(postId, "thumb");

        result.put("post", post);
        result.put("fileList", fileList);
        result.put("thumbList", thumbList);
        return result;

    
    }

    // 공지사항, FAQ 저장
    public Integer createPost(Map<String, Object> params, HttpServletRequest request) throws Exception {
        if(params.get("post_content") != null){
            params.put("post_content", sanitizeCustomHtml(params.get("post_content").toString()));
        }
        
        Integer result = postMapper.insertPost(params);
        return result;
    }

    // 공지사항, FAQ 수정
    public Object updatePost(Map<String, Object> params, List<String> delete_files) throws Exception {
        if(params.get("post_content") != null){     
            params.put("post_content", sanitizeCustomHtml(params.get("post_content").toString()));
        }

        Integer result = postMapper.updatePost(params);
        if ( !ObjectUtils.isEmpty(delete_files) ) {
            for (String item : delete_files) {
                fileMapper.deleteFile(item);
            }
        }
        return result;
    }

    // 활용사례 썸네일 삭제
    public void deleteThumb(Map<String, Object> params) throws Exception {
        fileMapper.deleteThumb((Integer) params.get("id"));                
        return;
    }

    // 공지사항, FAQ 삭제
    public Object deletePost(Map<String, Object> params) throws Exception {
        Integer result = 0;
        String postType = (String) params.get("post_type");
        ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("deleteList");
        for (Integer id : deleteList) {
            result += postMapper.deletePost(id, postType);
        }

        return result;
    }

    // FAQ 순서 저장
    public Object updatePostFaqOrdNo(Map<String, Object> params) throws Exception {
        Integer result = 0;
        ArrayList<HashMap<String, Object>> postList = (ArrayList<HashMap<String, Object>>) params.get("postList");
        for (HashMap<String, Object> item : postList) {
            if(item.get("post_content") != null){                
                item.put("post_content", sanitizeCustomHtml(item.get("post_content").toString()));
            }

            result += postMapper.updatePostFaqOrdNo(item);
        }

        return result;
    }

    /**
     * 커스터마이징한 Safelist 사용 예시
     */
    public String sanitizeCustomHtml(String htmlContent) {
        // 허용할 태그와 속성을 지정한 커스텀 Safelist
        // 기본 Safelist에 태그와 속성 추가
        Safelist safelist = Safelist.basic()
            // 텍스트 스타일링 태그
            .addTags("h1", "h2", "h3", "h4", "h5", "h6", "blockquote", "p", "pre", "code", "ul", "ol", "li", "br", "hr")
            // 인라인 태그
            .addTags("b", "i", "strong", "em", "u", "strike", "a", "span", "small", "sub", "sup", "mark", "abbr")
            // 테이블 태그
            .addTags("table", "thead", "tbody", "tr", "th", "td")
            // 이미지 및 미디어 관련 태그
            // .addTags("img", "figure", "figcaption", "iframe", "embed")
            .addTags("img", "figure", "figcaption")
            // 허용할 속성
            .addAttributes("a", "href", "title")
            .addAttributes("img", "src", "alt", "title", "width", "height")
            .addAttributes("figure", "width", "height")
            .addAttributes(":all", "style", "class") // 모든 태그에 대해 class, style 속성 허용
            // .addAttributes("iframe", "src", "width", "height", "allowfullscreen", "frameborder")
            // .addAttributes("embed", "src", "type", "width", "height", "allowfullscreen", "frameborder")
            // URL 관련 프로토콜 제한 (보안 강화)
            .addProtocols("a", "href", "http", "https", "mailto");
            //   .addProtocols("img", "src", "http", "https");
            // .addProtocols("iframe", "src", "http", "https")
            // .addProtocols("embed", "src", "http", "https");

        // Jsoup을 사용하여 허용된 태그만 남기고 정리
        return Jsoup.clean(htmlContent, safelist);
    }
}
