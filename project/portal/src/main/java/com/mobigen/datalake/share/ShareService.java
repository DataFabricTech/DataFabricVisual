package com.mobigen.datalake.share;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {

    private final ShareMapper shareMapper;
    private final FileMapper fileMapper;

    @Value("${properties.activeSendMail.data-request}")
	public boolean activeSendMail;
    
    // 데이터요청 목록 전체개수 조회
    public int getShareListTotal(Map<String, String> param) throws Exception {
        int total = shareMapper.getShareListTotal(param);
        return total;
    }
    
    // 데이터요청 목록 조회
    public List<Map> getShareList(Map<String, String> param) throws Exception {
        List<Map> dataRequestList = (List<Map>) shareMapper.getShareList(param);
        return dataRequestList;
    }

    // 활용사례 단건 조회
    public Map<String, Object> getShareOne(String id, HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> share = shareMapper.getShareOne(id);

        List<Map> fileList = (List<Map>) fileMapper.getFileList(id, "file");
        List<Map> thumbList = (List<Map>) fileMapper.getFileList(id, "thumb");
           
        result.put("share", share);
        result.put("fileList", fileList);
        result.put("thumbList", thumbList);
        return result;

    
    }

//     // 데이터 요청 url 접근 시 작성자 여부 확인
//     public Boolean isWriter(String writer, Map<String, Object> user) throws Exception {
//         String logonId = user.get("user_id").toString();
//         String roleName = user.get("authority").toString();

//         Boolean result = false;

//         // 관리자인 경우, 작성자인 경우 true
//         if (roleName.equals("Admin")
//          || roleName.equals("Super-Admin")
//          || logonId.equals(writer)) {
//             result = true;
//         }

//         return result;
//     }

    // 활용사례 등록
    public Integer createShare(Map<String, Object> params, HttpServletRequest request) throws Exception {
        Integer result = 0;

        try {
            if(params.get("contents") != null){
                params.put("contents", sanitizeCustomHtml(params.get("contents").toString()));
            }
            
            result = shareMapper.insertShare(params);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("데이터 요청 등록에 실패하였습니다.");
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

    // 활용사례 삭제
    public Object deleteShare(Map<String, Object> params) throws Exception {
        Integer result = 0;
        ArrayList<Integer> deleteList = (ArrayList<Integer>) params.get("deleteList");
        for (Integer id : deleteList) {
            result += shareMapper.deleteShare(id);
        }

        return result;
    }

    // 활용사례 등록시 정보
    public List<Map<String, Object>> getShareFileInfoList(Integer post_id) throws Exception {
        List<Map<String, Object>> result = shareMapper.getShareFileInfoList(post_id);
        if (result == null) result = new ArrayList<Map<String, Object>>();
        return result;
    }

    // 활용사례 수정
    public Object updateShare(Map<String, Object> params, List<String> delete_files) throws Exception {
        if(params.get("contents") != null){     
            params.put("contents", sanitizeCustomHtml(params.get("contents").toString()));
        }

        Integer result = shareMapper.updateShare(params);
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
}
