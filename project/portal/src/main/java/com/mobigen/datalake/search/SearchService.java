package com.mobigen.datalake.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;

    // 검색 이력 저장
    public void insertSearchTextHistory(Map<String, Object> textHistoryParam) {
        searchMapper.insertSearchTextHistory(textHistoryParam);
    }

    public List<String> getSystemtypes() {
        return searchMapper.getSystemtypes();
    }

    public List<String> getMetatypes() {
        return searchMapper.getMetatypes();
    }

    // hive_table 전체 개수 조회
    public int getTableTotal() {
        return searchMapper.getTableTotal();
    }

}
