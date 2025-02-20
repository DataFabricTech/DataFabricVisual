package com.mobigen.datalake.main;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;
    private final SessionManager sessionManager;

    // 추천 검색어 조회
    @ResponseJsonResult
    @GetMapping("/recommendedWordList")
    public Object getRecommendedWordList() throws Exception {
        return mainPageService.getRecommendedWordList();
    }

    // 전체 데이터 수, 데이터 활용 횟수, 신규 데이터
    @ResponseJsonResult
    @GetMapping("/countData")
    public Object getDataCountList() throws Exception {
        return mainPageService.getDataCountData();
    }

    // 공지사항, FAQ 조회
    @ResponseJsonResult
    @GetMapping("/postList")
    public Object getPostList() throws Exception {
        return mainPageService.getPostListData();
    }

    // 활용사례 조회
    @ResponseJsonResult
    @GetMapping("/shareDataList")
    public Object getShareDataList() throws Exception {
        return mainPageService.getShareDataList();
    }

    // 최신 등록 데이터 목록 조회
    @ResponseJsonResult
    @GetMapping("/latestDataList")
    public Object getLatestDataList(HttpServletRequest request) throws Exception {
        return mainPageService.getLatestDataList(request);
    }

    // My 데이터 요청 조회
    @ResponseJsonResult
    @GetMapping("/myRequestDataList")
    public Object getMyRequestDataList(HttpServletRequest request) throws Exception {
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        userId = user.get("user_id").toString();
        return mainPageService.getMyRequestDataList(userId);
    }

    // My 찜 데이터 조회
    @ResponseJsonResult
    @GetMapping("/myFavoriteData")
    public Object getMyFavoriteDataList(HttpServletRequest request) throws Exception {
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        return mainPageService.getMyFavoriteDataList(userId);
    }

    // My 리뷰데이터 조회
    @ResponseJsonResult
    @GetMapping("/myReviewData")
    public Object getMyReviewDataList(HttpServletRequest request) throws Exception {
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        return mainPageService.getMyReviewDataList(userId);
    }

    // My 데이터 추출 내역 조회
    @ResponseJsonResult
    @GetMapping("/myExtractedData")
    public Object getMyExtractedDataList(HttpServletRequest request) throws Exception {
        // 세션 사용자 ID와 실제 DB의 게시글 작성자 일치 여부 확인
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        String userId = user.get("user_id").toString();
        return mainPageService.getMyExtractedDataList(userId);
    }

    // 카테고리 항목 조회 (스와이프 차트)
    @ResponseJsonResult
    @GetMapping("/getChartCategoryList")
    public Object getChartCategoryList(HttpServletRequest request) throws Exception {
        return mainPageService.getChartCategoryList();
    }

    // 메인화면 배너 제목 조회
    @ResponseJsonResult
    @GetMapping("/mainBannerSubject")
    public Object getMainBannerSubject(HttpServletRequest request) throws Exception {
        return mainPageService.getMainBannerSubject();
    }

    // 레이아웃 관리 목록 조회
    @ResponseJsonResult
    @GetMapping("/layoutList")
    public Object getLayoutList(HttpServletRequest request) throws Exception {
        return mainPageService.getLayoutList();
    }

    // 레이아웃 관리 적용 저장
    @ResponseJsonResult
    @PostMapping("/updateCodeTable")
    public Object updateCodeTable(@RequestBody Map<String, Object> params) throws Exception {
        return mainPageService.updateCodeTable(params);
    }

    // 레이아웃 관리 목록 조회
    @ResponseJsonResult
    @GetMapping("/footerList")
    public Object getFooterList(HttpServletRequest request) throws Exception {
        return mainPageService.getFooterList();
    }
}
