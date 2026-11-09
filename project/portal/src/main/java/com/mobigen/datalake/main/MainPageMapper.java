package com.mobigen.datalake.main;

import java.util.List;
import java.util.Map;

public interface MainPageMapper {
    List getRecommendedWordList() throws Exception;

    int getTotalDataCount() throws Exception;

    int getUseDataCount() throws Exception;

    List getNoticeList() throws Exception;

    List getFaqList() throws Exception;

    List getShareDataList() throws Exception;

    List getPopularDataList() throws Exception;

    List getRecommendDataList() throws Exception;

    List getMyRequestDataList(String user_id) throws Exception;

    List getMyFavoriteDataList(String user_id) throws Exception;

    List getMyReviewDataList(String user_id) throws Exception;

    List getMyExtractedDataList(String user_id) throws Exception;

    List getChartCategoryList() throws Exception;

    List<Map<String, Object>> getMainBannerSubject() throws Exception;

    int deleteCodeTableFromName(Map<String, Object> params) throws Exception;

    int updateCodeTableFromName(Map<String, Object> params) throws Exception;

    int insertCodeTable(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> getLayoutList() throws Exception;

    List<Map<String, Object>> getFooterList() throws Exception;

    Map<String, Object> getCodeInfo(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> getMetaTypeList() throws Exception;
}