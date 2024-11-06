package com.mobigen.datalake.search;

import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.*;
import java.util.concurrent.*;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;

    @Value("${properties.trino.hikari.jdbc-url}")
    private String trinoJdbcUrl;

    // 검색 이력 저장
    public void insertSearchTextHistory(Map<String, Object> textHistoryParam) {
        searchMapper.insertSearchTextHistory(textHistoryParam);
    }

    // hive_table 전체 개수 조회
    public int getTableTotal() {
        return searchMapper.getTableTotal();
    }

    // 외부 데이터 조회
    public Map<String, Object> getExternalDataList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 외부데이터 항목 조회
        List<Map<String, Object>> externalDataList = searchMapper.getExternalDataList(params);

        result.put("list", externalDataList);
        result.put("total", searchMapper.getExternalDataListTotal(params));
        return result;
    }

    // 외부 데이터 카운트 조회
    public int getExternalDataListCount(Map<String, Object> params) throws Exception {
        return searchMapper.getExternalDataListTotal(params);
    }

    // 카테고리, 시스템 순서 정렬
    public List<Map<String, Object>> processSysCatSort(List<Map<String, Object>> sortedList) throws Exception {

        if (sortedList != null) {
            // 1. 카테고리, 시스템이 들어있는 항목 확인
            for (int a=0; a < sortedList.size(); a++) {
                List<String> classificationNames = (List<String>) ((List<Map<String, Object>>) sortedList).get(a).get("classificationNames");
                List<String> sysList = new ArrayList<>();
                List<String> catList = new ArrayList<>();
                List<String> newClassificationNames = new ArrayList<>();
                for (int i=0; i<classificationNames.size(); i++) {
                    if (classificationNames.get(i) != null) {
                        String cn = classificationNames.get(i);
                        // 2. 시스템 항목 추출
                        if (cn.startsWith("SYS")) {
                            sysList.add(cn);                        
                        }

                        // 3. 카테고리 항목 추출
                        if (cn.startsWith("CAT")) {
                            catList.add(cn);                        
                        }
                    }
                }

                // 4. 시스템 항목, 카테고리 항목 1개씩 추가
                if (sysList.size() > 0) {
                    Collections.sort(sysList);
                    newClassificationNames.addAll(sysList);
                }
                if (catList.size() > 0) {
                    Collections.sort(catList);
                    newClassificationNames.addAll(catList);
                }

                // 3. classificationNames에 덮어쓰기
                ((List<Map<String, Object>>) sortedList).get(a).put("classificationNames", newClassificationNames);
            }
        }
        
        return sortedList;
    }

    public List<Map<String, Object>> checkDataAccessByEntities(List<Map<String, Object>> entities, Map<String, Object> user) {
        long startTime = System.nanoTime();
        if (entities != null) {
            // 스레드 풀 생성 및 작업 실행 (이전 코드와 동일)
            // 각 스레드에서 dataSource.getConnection()으로 Connection 획득
            ExecutorService executorService = Executors.newFixedThreadPool(10); // 스레드 풀 크기 조절

            List<Future<Void>> futures = new ArrayList<>();

            DataSource dataSource = createDatasSource(user.get("user_id").toString());

            for (int i = 0; i < entities.size(); i++) {
                final int index = i;
                Callable<Void> task = () -> {
                    Map<String, Object> entity = entities.get(index);
                    Map<String, Object> attributes = (Map<String, Object>) entity.get("attributes");

                    Map<String, Object> param = new LinkedHashMap<>();
                    param.put("type_name", entity.get("typeName"));
                    param.put("qualified_name", attributes.get("qualifiedName"));

                    Boolean dataAccess = checkDataAccess(param, dataSource);
                    entity.put("dataAccess", dataAccess);

                    return null;
                };

                Future<Void> future = executorService.submit(task);
                futures.add(future);
            }

            // 모든 작업 완료 대기
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            executorService.shutdown();
        }
        

        long endTime = System.nanoTime();
        long durationInNano = endTime - startTime;
        double durationInSeconds = durationInNano / 1_000_000_000.0;
        log.info("전체 프로세스 실행 시간: " + durationInSeconds + " 초");        

        return entities;
    }    

    public Boolean checkDataAccess(Map<String, Object> params, DataSource dataSource) {
        long startTime = System.nanoTime();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // 쿼리 생성 및 실행
            String query = createQuery(params);
            if(query.length() > 0){
                try (ResultSet rs = statement.executeQuery(query)) {
                    return true;
                } catch (SQLException e) {
                    if (e.getMessage().contains("Access Denied")) {
                        return false;
                    } else {
                        e.printStackTrace();
                        return false;
                    }
                }
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            long endTime = System.nanoTime();
            long durationInNano = endTime - startTime;
            double durationInSeconds = durationInNano / 1_000_000_000.0;
            log.info(": 쿼리 실행 시간 " + durationInSeconds + " 초");
        }
    }

    private String createQuery(Map<String, Object> params) {
        String query = "";

        if (params.get("type_name").toString().endsWith("_table")) {
        
            // 테이블 이름 추출 및 쿼리 생성
            String[] queryStrSplit = params.get("qualified_name").toString().split("@");
            String tableNameStr = queryStrSplit[0];
            String[] tableNameArr = tableNameStr.split("\\.");
            StringBuilder tableName = new StringBuilder();
            for (int i = 0; i < tableNameArr.length; i++) {
                if (i > 0) {
                    tableName.append(".");
                }
                tableName.append("\"").append(tableNameArr[i]).append("\"");
            }

            query = "SELECT 1 FROM " + tableName + " LIMIT 1";
        }
        return query;
    }

    public DataSource createDatasSource(String userId) {
        // HikariCP 설정
        DataSource dataSource;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(trinoJdbcUrl);
        config.setUsername(userId);        
        config.addDataSourceProperty("SSL", "false");
        dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
