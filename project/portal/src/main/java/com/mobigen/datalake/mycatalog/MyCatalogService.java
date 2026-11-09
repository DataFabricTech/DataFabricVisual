package com.mobigen.datalake.mycatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyCatalogService {
    private final MyCatalogMapper myCatalogMapper;

    // My 조회 데이터
    public List<HashMap<String, Object>> getSearchDataList(Map<String, String> param) {
        return myCatalogMapper.getSearchDataList(param);
    }

    // My 데이터 요청 내역 목록 조회
    public List<HashMap<String, Object>> getDataRequestList(Map<String, String> param) {
        return myCatalogMapper.getDataRequestList(param);
    }

    // My 데이터 요청 내역 목록 전체개수 조회
    public int getDataRequestListCount(Map<String, String> param) {
        return myCatalogMapper.getDataRequestListCount(param);
    }

    // My 동의어 삭제 요청 내역 목록 조회
    public List<Map<String, Object>> getSynonymDeleteRequestList(Map<String, String> param) {
        return myCatalogMapper.getSynonymDeleteRequestList(param);
    }

    // My 전체 추출 데이터 목록 조회
    public List<Map<String, String>> getAllExtractedDataList(Map<String, String> param) {
        return myCatalogMapper.getAllExtractedDataList(param);
    }

    // My 전체 추출 데이터 목록 전체개수 조회
    public int getAllExtractedDataListCount(Map<String, String> param) {
        return myCatalogMapper.getAllExtractedDataListCount(param);
    }

    // My 추출 데이터 목록 조회
    public List<Map<String, String>> getExtractedDataList(Map<String, String> param) {
        List<Map<String, String>> resultList = new ArrayList<>();

        if (param.get("select_type") != null && param.get("select_type").toString().equals("1")) {
            resultList = myCatalogMapper.getExtractedBigDataList(param);
        } else {
            resultList = myCatalogMapper.getExtractedDataList(param);
        }

        return resultList;
    }

    // My 추출 데이터 목록 전체개수 조회
    public int getExtractedDataListCount(Map<String, String> param) {
        int resultCount = 0;
        if (param.get("select_type") != null && param.get("select_type").toString().equals("1")) {
            resultCount = myCatalogMapper.getExtractedBigDataListCount(param);
        } else {
            resultCount = myCatalogMapper.getExtractedDataListCount(param);
        }
        return resultCount;
    }

    // My 추출 데이터 대용량 추출 목록 전체개수 조회
    public int getExtractedBigDataListCount(Map<String, String> param) {
        return myCatalogMapper.getExtractedBigDataListCount(param);
    }

    // My 찜 데이터 목록 조회
    public List<Map<String, String>> getFavoriteDataList(Map<String, String> param) {
        return myCatalogMapper.getFavoriteDataList(param);
    }

    // My 찜 데이터 목록 전체개수 조회
    public int getFavoriteDataListCount(Map<String, String> param) {
        return myCatalogMapper.getFavoriteDataListCount(param);
    }

    // My 추출 데이터 목록 조회
    public List<Map<String, String>> getMyReviewDataList(Map<String, String> param) {
        return myCatalogMapper.getMyReviewDataList(param);
    }

    // My 추출 데이터 목록 전체개수 조회
    public int getMyReviewDataListCount(Map<String, String> param) {
        return myCatalogMapper.getMyReviewDataListCount(param);
    }

    public Object getMySynonymDeleteRequestList(Map<String, String> param) {
        return myCatalogMapper.getMySynonymDeleteRequestList(param);
    }
}
