package com.mobigen.datalake.mycatalog;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyCatalogService {
    private final MyCatalogMapper myCatalogMapper;

    // My 조회 데이터, 데이터 요청 내역 목록 조회
    public List<HashMap<String, Object>> getSearchDataList(Map<String, String> param) {
        return myCatalogMapper.getSearchDataList(param);
    }

    // My 동의어 삭제 요청 내역 목록 조회
    public List<Map<String, Object>> getSynonymDeleteRequestList(Map<String, String> param) {
        return myCatalogMapper.getSynonymDeleteRequestList(param);
    }

    // My 추출 데이터 목록 조회
    public List<Map<String,String>> getExtractedDataList(Map<String, String> param) {
        return myCatalogMapper.getExtractedDataList(param);
    }
    
    // My 추출 데이터 목록 전체개수 조회
    public int getExtractedDataListCount(Map<String, String> param) {
        return myCatalogMapper.getExtractedDataListCount(param);
    }

    // My 찜 데이터 목록 조회
    public List<Map<String,String>> getWishedDataList(Map<String, String> param) {
        return myCatalogMapper.getWishedDataList(param);
    }

    // My 찜 데이터 목록 전체개수 조회
    public int getWishedDataListCount(Map<String, String> param) {
        return myCatalogMapper.getWishedDataListCount(param);
    }

    // My 추출 데이터 목록 조회
    public List<Map<String, String>> getSuggestedDataList(Map<String, String> param) {
        return myCatalogMapper.getSuggestedDataList(param);
    }
    
    // My 추출 데이터 목록 전체개수 조회
    public int getSuggestedDataListCount(Map<String, String> param) {
        return myCatalogMapper.getSuggestedDataListCount(param);
    }

    public Object getMySynonymDeleteRequestList(Map<String, String> param) {
        return myCatalogMapper.getMySynonymDeleteRequestList(param);
    }
}
