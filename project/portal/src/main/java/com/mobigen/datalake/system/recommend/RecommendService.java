package com.mobigen.datalake.system.recommend;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RecommendService {
    private final RecommendMapper recommendMapper;

    // 추천 검색어 리스트 조회
    public List<Map> getRecommendSearchList() throws Exception {
        List<Map> recommendSearchList = (List<Map>) recommendMapper.getRecommendSearchList();
        return recommendSearchList;
    }

    // 추천 검색어 저장
    public Object saveRecommendSearchList(Map<String, Object> param) throws Exception {
        Integer result = 0;

        List<Map<String, String>> recommendSearchList = (List<Map<String, String>>) param.get("recommendSearchList");

        for (Map<String, String> item : recommendSearchList) {
            result += recommendMapper.saveRecommendSearchList(item);
        }
        return result;
    }
}
