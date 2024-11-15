package com.mobigen.datalake.search;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.RegexUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/search/detail")
@RestController
@AllArgsConstructor
public class DetailController {

    private final SessionManager sessionManager;
    private final SearchDetailService searchDetailService;

    /**
     * 상세 데이터 조회
     *
     * @param guid              조회용 메타 ID
     * @return Object           상세 데이터
     */
    @ResponseJsonResult
    @GetMapping("/{guid}")
    public Object getDetailData(@PathVariable("guid") String guid, HttpServletRequest request) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        params.put("guid", guid);
        return searchDetailService.getDetailData(params);
    }

    /**
     * 상세 데이터 조회 - 메타데이터 계산
     *
     * @param guid              조회용 메타 ID
     * @return Object           상세 데이터
     */
    @ResponseJsonResult
    @GetMapping("/metadatainfo/{guid}")
    public Object getDetailMetadatainfo(@PathVariable("guid") String guid, HttpServletRequest request) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id").toString());
        params.put("guid", guid);
        return searchDetailService.getDetailMetadatainfo(params);
    }

    /**
     * 테이블 프로파일 데이터 조회
     *
     * @param params            조회용 파라미터
     * @return Object           테이블 프로파일 정보
     */
    @ResponseJsonResult
    @PostMapping("/dataprofile")
    @ResponseBody
    public Object postDetailDataProfile(@RequestBody Map<String, Object> params) {
        return searchDetailService.getDetailDataProfile(params);
    }

    /**
     * 샘플 데이터 조회
     *
     * @param params            조회용 파라미터
     * @return Object           샘플 데이터 10건
     */
    @ResponseJsonResult
    @PostMapping("/sampledata")
    @ResponseBody
    public Object postSampleData(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("user_id", user.get("user_id"));

        // qualifiedName 검증
        String qualifiedName = params.get("qualifiedName").toString();
        RegexUtils.sanitizeInput(qualifiedName);

        return searchDetailService.getSampleData(params);
    }

    /**
     * 데이터 테이블 프로파일링 카운트 조회
     *
     * @param guid            조회용 guid
     * @return Object         개수
     */
    @ResponseJsonResult
    @GetMapping("/getRowCount")
    @ResponseBody
    public Object getRowCount(@RequestParam String guid, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        return searchDetailService.getRowCount(guid);
    }


    /**
     * 사용자 데이터 설명 목록 조회
     *
     * @param params            조회용 파라미터
     * @return Object           사용자 데이터 설명 목록
     */
    @ResponseJsonResult
    @PostMapping("/userdescriptionList")
    @ResponseBody
    public Object getUserDescriptionList(@RequestBody Map<String, Object> params) {
        return searchDetailService.getUserDescriptionList(params);
    }

    /**
     * 사용자 데이터 설명 등록
     *
     * @param params            등록용 파라미터
     * @return Object           사용자 데이터 설명 등록 결과
     */
    @ResponseJsonResult
    @PostMapping("/userdescriptionInsert")
    @ResponseBody
    public Object insertUserDescription(@RequestBody Map<String, Object> params) {
        return searchDetailService.insertUserDescription(params);
    }

    /**
     * 사용자 데이터 설명 수정
     *
     * @param params            수정용 파라미터
     * @return Object           사용자 데이터 설명 수정 결과
     */
    @ResponseJsonResult
    @PutMapping("/userdescriptionUpdate")
    @ResponseBody
    public Object updateUserDescription(@RequestBody Map<String, Object> params) {
        return searchDetailService.updateUserDescription(params);
    }

    /**
     * 사용자 데이터 설명 삭제
     *
     * @param params            삭제용 파라미터
     * @return Object           사용자 데이터 설명 삭제 결과
     */
    @ResponseJsonResult
    @PostMapping("/userdescriptionDelete")
    @ResponseBody
    public Object deleteUserDescription(@RequestBody Map<String, Object> params) {
        return searchDetailService.deleteUserDescription(params);
    }

    /**
     * 사용자 데이터 설명 추천 상태 1건 조회
     *
     * @param id                조회용 id
     * @return Object           사용자 데이터 설명 추천 상태 1건
     */
    @ResponseJsonResult
    @GetMapping("/userdescriptionFavoriteStatus/{id}")
    @ResponseBody
    public Object getUserDescriptionFavoriteStatus(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("id", id);
        params.put("userID", user.get("user_id"));
        return searchDetailService.getUserDescriptionFavoriteStatus(params);
    }

    /**
     * 사용자 데이터 설명 추천 등록
     *
     * @param params            등록용 파라미터
     * @return Object           사용자 데이터 설명 추천 등록 결과
     */
    @ResponseJsonResult
    @PostMapping("/userdescriptionFavoriteInsert")
    @ResponseBody
    public Object insertUserDescriptionFavorite(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("userID", user.get("user_id"));
        return searchDetailService.insertUserDescriptionFavorite(params);
    }

    /**
     * 사용자 데이터 설명 추천 삭제
     *
     * @param params            삭제용 파라미터
     * @return Object           사용자 데이터 설명 추천 취소 결과
     */
    @ResponseJsonResult
    @PostMapping("/userdescriptionFavoriteDelete")
    @ResponseBody
    public Object deleteUserDescriptionFavorite(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        params.put("userID", user.get("user_id"));
        return searchDetailService.deleteUserDescriptionFavorite(params);
    }

    /**
     * 사용자 리뷰 목록 조회
     *
     * @param param             조회용 파라미터
     * @return Object           사용자 리뷰 목록
     */
    @ResponseJsonResult
    @PostMapping("/reviewdatalist")
    @ResponseBody
    public Object postReviewDataList(@RequestBody Map<String, Object> param) {
        return searchDetailService.getReviewData((String) param.get("guid"));
    }

    /**
     * 사용자 리뷰 등록
     *
     * @param params            등록용 파라미터
     * @return Object           사용자 리뷰 등록 결과
     */
    @ResponseJsonResult
    @PostMapping("/review")
    @ResponseBody
    public Object insertUserReview(@RequestBody Map<String, Object> params) {
        return searchDetailService.insertUserReview(params);
    }

    /**
     * 사용자 리뷰 수정
     *
     * @param params            수정용 파라미터
     * @return Object           사용자 리뷰 수정 결과
     */
    @ResponseJsonResult
    @PutMapping("/review")
    @ResponseBody
    public Object updateUserReview(@RequestBody Map<String, Object> params) {
        return searchDetailService.updateUserReview(params);
    }

    /**
     * 사용자 리뷰 삭제
     *
     * @param reviewID          삭제용 id
     * @return Object           사용자 리뷰 삭제 결과
     */
    @ResponseJsonResult
    @DeleteMapping("/review/{reviewID}")
    @ResponseBody
    public Object deleteUserReview(@PathVariable("reviewID") int reviewID) {
        return searchDetailService.deleteUserReview(reviewID);
    }

    /**
     * 데이터 추천 & 찜하기 여부 조회
     *
     * @param params            조회용 파라미터
     * @return Object           데이터 추천 & 찜하기 여부 정보
     */
    @ResponseJsonResult
    @PostMapping("/recommendStatus")
    @ResponseBody
    public Object getRecommendStatus(@RequestBody Map<String, Object> params) {
        return searchDetailService.getRecommendStatus(params);
    }

    /**
     * 데이터 추천 등록
     *
     * @param params            등록용 파라미터
     * @return Object           데이터 추천 등록 결과
     */
    @ResponseJsonResult
    @PostMapping("/recommend")
    @ResponseBody
    public Object saveDataRecommendation(@RequestBody Map<String, Object> params) {
        return searchDetailService.saveDataRecommendation(params);
    }

    /**
     * 데이터 찜하기 등록
     *
     * @param params            등록용 파라미터
     * @return Object           데이터 찜하기 등록 결과
     */
    @ResponseJsonResult
    @PostMapping("/favorite")
    @ResponseBody
    public Object saveDataFavorite(@RequestBody Map<String, Object> params) {
        return searchDetailService.saveDataFavorite(params);
    }

    /**
     * 비즈니스 속성 Atlas에 등록
     *
     * @param params            등록용 파라미터
     * @return Object           비지니스 속성 Atlas에 등록 결과
     */
    @ResponseJsonResult
    @PostMapping("/attributes")
    @ResponseBody
    public Object saveEntityAttributes(@RequestBody Map<String, Object> params) {
        return searchDetailService.saveEntityAttributes(params);
    }

    /**
     * 데이터 담당 부서 조회
     *
     * @param params            조회용 파라미터
     * @return Object           데이터 담당 부서 목록
     */
    @ResponseJsonResult
    @PostMapping("/getDataManagerInfoList")
    @ResponseBody
    public Object getDataManagerInfoList(@RequestBody Map<String, Object> params) {
        return searchDetailService.getDataManagerInfoList(params);
    }
}
