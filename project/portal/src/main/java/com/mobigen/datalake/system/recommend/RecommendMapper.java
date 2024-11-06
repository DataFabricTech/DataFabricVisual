package com.mobigen.datalake.system.recommend;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("recommendMapper")
public interface RecommendMapper {
    List getRecommendSearchList() throws Exception;
    Integer saveRecommendSearchList(Map<String, String> param) throws Exception;
}