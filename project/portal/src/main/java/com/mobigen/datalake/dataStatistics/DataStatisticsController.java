package com.mobigen.datalake.dataStatistics;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    /**
     * 이력 목록 조회
     * @param searchType                검색 종류
     * @param startDate                 시작 날짜
     * @param endDate                   종료 날짜
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/datastat/history")
    public Object getHistoryList(@RequestParam("pageSize") int pageSize,
                                @RequestParam("pnum") int pnum,
                                @RequestParam("historyType") String historyType,
                                @RequestParam("searchType") String searchType,
                                @RequestParam("searchWord") String searchWord,
                                @RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("historyType", historyType);
        params.put("searchType", searchType);
        params.put("searchWord", searchWord);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("limit", String.valueOf(pageSize));
        params.put("offset", String.valueOf(pageSize * (pnum - 1)));

        return dataStatisticsService.getHistoryList(params);
    }

    /**
     * 이력 목록 조회
     * @param searchType                검색 종류
     * @param startDate                 시작 날짜
     * @param endDate                   종료 날짜
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/datastat/downloadHistory")
    public void downloadHistory(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        dataStatisticsService.downloadHistory(params, request, response);  
    }

}
