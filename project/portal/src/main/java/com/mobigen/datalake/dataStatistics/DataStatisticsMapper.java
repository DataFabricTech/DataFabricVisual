package com.mobigen.datalake.dataStatistics;

import java.util.List;

public interface DataStatisticsMapper {
    /**
     * 오늘 로그인 사용자 수
     * @return
     * @throws Exception
     */
    List getTodayTotal() throws Exception;

    /**
     * 총접속자수, 오늘접속자수, 어제접속자수, 이번달접속자수, 지난달접속자수 정보 조회
     * @param searchDateType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getLoginTotalInfo(String searchDateType, String startDate, String endDate) throws Exception;

    /**
     * 총 메타데이터 조회수, 오늘 메타데이터 조회수, 어제 메타데이터 조회수, 이번달 메타데이터 조회수, 지난달 메타데이터 조회수 정보 조회
     * @param searchDateType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getMetaTotalInfo(String searchDateType, String startDate, String endDate) throws Exception;

    /**
     * 총 데이터추출 수, 오늘 데이터추출 수, 어제 데이터추출 수, 이번달 데이터추출 수, 지난달 데이터추출 수 정보 조회
     * @param searchType
     * @param searchDateType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getFileApiTotalInfo(String searchType, String searchDateType, String startDate, String endDate) throws Exception;

    /**
     * 총 데이터요청 수, 오늘 데이터요청 수, 어제 데이터요청 수, 이번달 데이터요청 수, 지난달 데이터요청 수 정보 조회
     * @param searchDateType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getDataRequestTotalInfo(String searchDateType, String startDate, String endDate) throws Exception;


    /**
     * 평균접속자수
     * @return
     * @throws Exception
     */
    Object getLoginAvg() throws Exception;
    /**
     * 차트데이터 조회 : 년도별 로그인 사용자 수
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getLoginYearChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 월별 로그인 사용자 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getLoginMonthChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 일별 로그인 사용자 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getLoginDayChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 시간대별 로그인 사용자 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getLoginHourChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 년도별 메타데이터 조회 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getMetaYearChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 월별 메타데이터 조회 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getMetaMonthChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 일별 메타데이터 조회 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getMetaDayChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 시간대별 메타데이터 조회 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getMetaHourChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 년도별 데이터 추출 수
     * @param searchType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getFileApiYearChartData(String searchType, String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 월별 데이터 추출 수
     * @param searchType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getFileApiMonthChartData(String searchType, String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 일별 데이터 추출 수
     * @param searchType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getFileApiDayChartData(String searchType, String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 시간대별 데이터 추출 수
     * @param searchType
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getFileApiHourChartData(String searchType, String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 년도별 데이터 요청 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getDataRequestYearChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 월별 데이터 요청 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getDataRequestMonthChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 일별 데이터 요청 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getDataRequestDayChartData(String startDate, String endDate) throws Exception;

    /**
     * 차트데이터 조회 : 시간대별 데이터 요청 수
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List getDataRequestHourChartData(String startDate, String endDate) throws Exception;



}
