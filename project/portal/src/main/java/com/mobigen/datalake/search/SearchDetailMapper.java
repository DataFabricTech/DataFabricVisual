package com.mobigen.datalake.search;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("searchDetailMapper")
public interface SearchDetailMapper {

    Map<String, Object> getTableData(String guid);

    Map<String, Object> getDbData(String guid);

    List<Map> getColumnData(List<String> relationData);

    Map<String, Object> getUserDescription(String guid);

    List<Map> getUserDescriptionList(Map<String, Object> params);

    Integer insertUserDescription(Map<String, Object> params);

    Integer updateUserDescription(Map<String, Object> params);

    Integer deleteUserDescription(int params);

    Integer getUserDescriptionFavoriteCounts(Map<String, Object> params);

    Map<String, Object> getUserDescriptionFavoriteStatus(Map<String, Object> params);

    Integer insertUserDescriptionFavorite(Map<String, Object> params);

    Integer addUserDescriptionFavoriteCount(int params);

    Integer deleteUserDescriptionFavorite(Map<String, Object> params);

    Integer subUserDescriptionFavoriteCount(int params);

    List<Map> getReviewData(String guid);

    Integer insertUserReview(Map<String, Object> params);

    Integer updateUserReview(Map<String, Object> params);

    Integer deleteUserReview(int params);

    Map<String, Object> getDataRecommendedByUser(Map<String, Object> params);

    Integer getDataRecommendedCounts(Map<String, Object> params);

    Map<String, Object> getDataFavoredByUser(Map<String, Object> params);

    Integer insertDataRecommendation(Map<String, Object> params);

    Integer deleteDataRecommendation(Map<String, Object> params);

    Integer insertDataFavorite(Map<String, Object> params);

    Integer deleteDataFavorite(Map<String, Object> params);

    // List<Map> getAuthCheckList(Map<String, Object> params);

    void insertSearchMetaHistory(Map<String, Object> saveData);

    List<Map<String,String>> selectImdbData();
}
