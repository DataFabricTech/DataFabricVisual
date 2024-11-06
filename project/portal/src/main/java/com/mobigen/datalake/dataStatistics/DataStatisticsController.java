package com.mobigen.datalake.dataStatistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class DataStatisticsController {
    private final DataStatisticsService dataStatisticsService;

    /**
     * 오늘의 통계 데이터 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/datastat/total")
    public Object getDataStatTotal() throws Exception {
        return dataStatisticsService.getDataStatTotal();
    }

    /**
     * 차트 데이터 조회
     * @param searchType                검색 종류
     * @param startDate                 시작 날짜
     * @param endDate                   종료 날짜
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/datastat/chart")
    public Object getDictionaryWordList(@RequestParam("searchType") String searchType, @RequestParam("searchDateType") String searchDateType, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        return dataStatisticsService.getChartDataList(searchType, searchDateType, startDate, endDate);
    }

}
