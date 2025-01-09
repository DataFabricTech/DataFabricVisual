package com.mobigen.datalake.main;

import java.util.List;

public interface MainPageMapper {
    List getRecommendedWordList() throws Exception;
    int getExternalTotalCount() throws Exception;
    int getTotalDataCount() throws Exception;
    int getUseDataCount() throws Exception;
    int getNewDataCount() throws Exception;
    List getNoticeList() throws Exception;
    List getFaqList() throws Exception;
    List getShareDataList() throws Exception;
    List getPopularDataList() throws Exception;
    List getRecommendDataList() throws Exception;
    List getMyRequestDataList(String user_id) throws Exception;
    List getMyFavoriteDataList(String user_id) throws Exception;
    List getChartCategoryList() throws Exception;
}