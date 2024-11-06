package com.mobigen.datalake.dataStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataStatisticsService {

    private final DataStatisticsMapper dataStatisticsMapper;

    /**
     * 오늘의 통계 데이터 조회
     * @return
     * @throws Exception
     */
    public List<Map> getDataStatTotal() throws Exception {
        return dataStatisticsMapper.getTodayTotal();//오늘 로그인 접속자, 메타데이터 조회, 파일추출, API 추출, 데이터 요청
    }

    /**
     * 차트 데이터 조회
     * @param searchType : login(로그인), meta(메타데이터), file(파일추출), api(API추출), request(데이터 요청)
     * @param searchDateType : year(년도별), month(월별), day(일별), hour(시간대별)
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public Map<String, Object> getChartDataList(String searchType, String searchDateType, String startDate, String endDate) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if("login".equals(searchType)) {//로그인 차트 데이터 조회
            if("year".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getLoginYearChartData(startDate, endDate));
            } else if("month".equals(searchDateType)) {
                result.put("chartDataList",  dataStatisticsMapper.getLoginMonthChartData(startDate, endDate));
            } else if("day".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getLoginDayChartData(startDate, endDate));
            } else if("hour".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getLoginHourChartData(startDate, endDate));
            }
            result.put("totalInfo", (List<Map>)dataStatisticsMapper.getLoginTotalInfo(searchDateType, startDate, endDate));
        } else if("meta".equals(searchType)) {//메타 차트 데이터 조회
            if("year".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getMetaYearChartData(startDate, endDate));
            } else if("month".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getMetaMonthChartData(startDate, endDate));
            } else if("day".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getMetaDayChartData(startDate, endDate));
            } else if("hour".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getMetaHourChartData(startDate, endDate));
            }
            result.put("totalInfo", (List<Map>)dataStatisticsMapper.getMetaTotalInfo(searchDateType, startDate, endDate));
        } else if("file".equals(searchType) || "api".equals(searchType)) {//데이터추출 차트 데이터 조회
            if("year".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getFileApiYearChartData(searchType, startDate, endDate));
            } else if("month".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getFileApiMonthChartData(searchType, startDate, endDate));
            } else if("day".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getFileApiDayChartData(searchType, startDate, endDate));
            } else if("hour".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getFileApiHourChartData(searchType, startDate, endDate));
            }
            result.put("totalInfo", (List<Map>)dataStatisticsMapper.getFileApiTotalInfo(searchType, searchDateType, startDate, endDate));
        } else if("request".equals(searchType)) {//데이터요청 차트 데이터 조회
            if("year".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getDataRequestYearChartData(startDate, endDate));
            } else if("month".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getDataRequestMonthChartData(startDate, endDate));
            } else if("day".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getDataRequestDayChartData(startDate, endDate));
            } else if("hour".equals(searchDateType)) {
                result.put("chartDataList",  (List<Map>) dataStatisticsMapper.getDataRequestHourChartData(startDate, endDate));
            }
            result.put("totalInfo", (List<Map>)dataStatisticsMapper.getDataRequestTotalInfo(searchDateType, startDate, endDate));
        }
        return result;
    }

}
