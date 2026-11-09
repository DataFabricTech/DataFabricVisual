package com.mobigen.datalake.dataStatistics;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataStatisticsService {

    private final DataStatisticsMapper dataStatisticsMapper;

    /**
     * 오늘의 통계 데이터 조회
     * 
     * @return
     * @throws Exception
     */
    public List<Map> getDataStatTotal() throws Exception {
        return dataStatisticsMapper.getTodayTotal();// 오늘 로그인 접속자, 메타데이터 조회, 파일추출, API 추출, 데이터 요청
    }

    /**
     * 차트 데이터 조회
     * 
     * @param searchType     : login(로그인), meta(메타데이터), file(파일추출), api(API추출),
     *                       request(데이터 요청)
     * @param searchDateType : year(년도별), month(월별), day(일별), hour(시간대별)
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public Map<String, Object> getChartDataList(String searchType, String searchDateType, String startDate,
            String endDate) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if ("login".equals(searchType)) {// 로그인 차트 데이터 조회
            if ("year".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getLoginYearChartData(startDate, endDate));
            } else if ("month".equals(searchDateType)) {
                result.put("chartDataList", dataStatisticsMapper.getLoginMonthChartData(startDate, endDate));
            } else if ("day".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getLoginDayChartData(startDate, endDate));
            } else if ("hour".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getLoginHourChartData(startDate, endDate));
            }
            result.put("totalInfo",
                    (List<Map>) dataStatisticsMapper.getLoginTotalInfo(searchDateType, startDate, endDate));
        } else if ("meta".equals(searchType)) {// 메타 차트 데이터 조회
            if ("year".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getMetaYearChartData(startDate, endDate));
            } else if ("month".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getMetaMonthChartData(startDate, endDate));
            } else if ("day".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getMetaDayChartData(startDate, endDate));
            } else if ("hour".equals(searchDateType)) {
                result.put("chartDataList", (List<Map>) dataStatisticsMapper.getMetaHourChartData(startDate, endDate));
            }
            result.put("totalInfo",
                    (List<Map>) dataStatisticsMapper.getMetaTotalInfo(searchDateType, startDate, endDate));
        } else if ("file".equals(searchType) || "api".equals(searchType)) {// 데이터추출 차트 데이터 조회
            if ("year".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getFileApiYearChartData(searchType, startDate, endDate));
            } else if ("month".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getFileApiMonthChartData(searchType, startDate, endDate));
            } else if ("day".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getFileApiDayChartData(searchType, startDate, endDate));
            } else if ("hour".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getFileApiHourChartData(searchType, startDate, endDate));
            }
            result.put("totalInfo", (List<Map>) dataStatisticsMapper.getFileApiTotalInfo(searchType, searchDateType,
                    startDate, endDate));
        } else if ("request".equals(searchType)) {// 데이터요청 차트 데이터 조회
            if ("year".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getDataRequestYearChartData(startDate, endDate));
            } else if ("month".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getDataRequestMonthChartData(startDate, endDate));
            } else if ("day".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getDataRequestDayChartData(startDate, endDate));
            } else if ("hour".equals(searchDateType)) {
                result.put("chartDataList",
                        (List<Map>) dataStatisticsMapper.getDataRequestHourChartData(startDate, endDate));
            }
            result.put("totalInfo",
                    (List<Map>) dataStatisticsMapper.getDataRequestTotalInfo(searchDateType, startDate, endDate));
        }
        return result;
    }

    public Map<String, Object> getHistoryList(Map<String, Object> params) throws Exception {
        Map resultMap = new HashMap<>();
        List list = new ArrayList<>();
        int total = 0;

        if (params.get("historyType") != null) {
            // 로그인 이력
            if (params.get("historyType").toString().equals("login")) {
                try {
                    total = dataStatisticsMapper.getLoginHistoryListTotal(params);
                    list = dataStatisticsMapper.getLoginHistoryList(params);

                } catch (Exception e) {
                    resultMap.put("list", new ArrayList<>());
                    resultMap.put("total", 0);
                }
            }

            // 다운로드 이력
            if (params.get("historyType").toString().equals("download")) {
                try {
                    total = dataStatisticsMapper.getDownloadHistoryListTotal(params);
                    list = dataStatisticsMapper.getDownloadHistoryList(params);

                } catch (Exception e) {
                    e.printStackTrace();
                    resultMap.put("list", new ArrayList<>());
                    resultMap.put("total", 0);
                }
            }

            // 상세조회 이력
            if (params.get("historyType").toString().equals("searchMeta")) {
                try {
                    total = dataStatisticsMapper.getSearchMetaHistoryListTotal(params);
                    list = dataStatisticsMapper.getSearchMetaHistoryList(params);

                } catch (Exception e) {
                    e.printStackTrace();
                    resultMap.put("list", new ArrayList<>());
                    resultMap.put("total", 0);
                }
            }

            // 데이터요청 이력
            if (params.get("historyType").toString().equals("dataRequest")) {
                try {
                    total = dataStatisticsMapper.getDataRequestHistoryListTotal(params);
                    list = dataStatisticsMapper.getDataRequestHistoryList(params);

                } catch (Exception e) {
                    e.printStackTrace();
                    resultMap.put("list", new ArrayList<>());
                    resultMap.put("total", 0);
                }
            }
        }

        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

    public void downloadHistory(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String historyType = params.get("historyType").toString();

        // 출력할 CSV 파일 이름
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDateTime = dateFormat.format(currentDate);
        String csvFileName = historyType + "_" + formattedDateTime + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + csvFileName + "\"");

        // 이력 조회 항목
        List<LinkedHashMap<String, Object>> historyList = null;
        if (historyType.equals("login")) {
            historyList = dataStatisticsMapper.getLoginHistoryList(params);
        } else if (historyType.equals("download")) {
            historyList = dataStatisticsMapper.getDownloadHistoryList(params);
        } else if (historyType.equals("searchMeta")) {
            historyList = dataStatisticsMapper.getSearchMetaHistoryList(params);
        } else if (historyType.equals("dataRequest")) {
            historyList = dataStatisticsMapper.getDataRequestHistoryList(params);
        }

        if (historyList != null && !historyList.isEmpty()) {
            try (OutputStream os = response.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                    PrintWriter writer = new PrintWriter(osw)) {

                // BOM 추가 (Excel 호환성을 위해)
                writer.write('\uFEFF');

                // 헤더 작성 (한글명 설정)
                Map<String, Object> firstRow = historyList.get(0);
                String[] headers = firstRow.keySet().toArray(new String[0]);
                Map<String, String> columnNameToKoreanMap = getKoreanHeaderMap(historyType);

                String[] koreanHeaders = Arrays.stream(headers)
                        .map(key -> columnNameToKoreanMap.getOrDefault(key, key)) // 키에 해당하는 한글명 매핑
                        .toArray(String[]::new);

                writer.println(String.join(",", koreanHeaders));

                // 데이터 작성
                for (Map<String, Object> row : historyList) {
                    for (int i = 0; i < headers.length; i++) {
                        Object value = row.get(headers[i]);
                        writer.print(value != null ? value.toString().replace(",", " ") : ""); // 데이터 내 쉼표 방지
                        if (i < headers.length - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }

                writer.flush();
            } catch (Exception e) {
                handleErrorResponse(response, "파일 다운로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
                throw new Exception("CSV 다운로드 실패", e);
            }
        } else {
            handleErrorResponse(response, "조회된 데이터가 없습니다.");
            throw new Exception("조회된 데이터가 없습니다.");
        }
    }

    private Map<String, String> getKoreanHeaderMap(String historyType) {
        Map<String, String> map = new HashMap<>();
        if (historyType.equals("login")) {
            map.put("rownum", "No.");
            map.put("id", "ID");
            map.put("user_id", "사용자ID");
            map.put("user_name", "사용자명");
            map.put("dept_id", "조직ID");
            map.put("dept_name", "조직명");
            map.put("user_ip_addr", "사용자IP");
            map.put("login_date", "로그인 시간");
        }

        if (historyType.equals("download")) {
            map.put("rownum", "No.");
            map.put("user_id", "사용자ID");
            map.put("user_name", "사용자명");
            map.put("dept_id", "조직ID");
            map.put("dept_name", "조직명");
            map.put("user_ip_addr", "사용자IP");
            map.put("meta_name", "데이터명");
            map.put("description", "데이터 설명");
            map.put("download_type", "다운로드 방식");
            map.put("download_date", "다운로드 시간");
        }

        if (historyType.equals("searchMeta")) {
            map.put("rownum", "No.");
            map.put("user_id", "사용자ID");
            map.put("user_name", "사용자명");
            map.put("dept_id", "조직ID");
            map.put("dept_name", "조직명");
            map.put("meta_name", "데이터명");
            map.put("description", "데이터 설명");
            map.put("search_date", "검색일");
        }

        if (historyType.equals("dataRequest")) {
            map.put("rownum", "No.");
            map.put("user_id", "사용자ID");
            map.put("user_name", "사용자명");
            map.put("dept_id", "조직ID");
            map.put("dept_name", "조직명");
            map.put("id", "요청 ID");
            map.put("title", "요청명");
            map.put("create_date", "요청일");
            map.put("process_status", "요청상태");
        }

        return map;
    }

    private void handleErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        Map<String, String> responseMap = new HashMap<>();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        responseMap.put("errorMessage", errorMessage);
        String errorResult = convertMapToJson(responseMap);
        response.getOutputStream().write(errorResult.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    // Map을 JSON 문자열로 변환하는 메서드
    private String convertMapToJson(Map<String, String> map) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            jsonBuilder.append("\"").append(entry.getKey()).append("\": ")
                    .append("\"").append(entry.getValue()).append("\", ");
        }

        // 마지막 쉼표 제거
        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
