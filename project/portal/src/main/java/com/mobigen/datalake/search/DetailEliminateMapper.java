package com.mobigen.datalake.search;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("detailEliminateMapper")
public interface DetailEliminateMapper {
    Integer insertDownloadHistory(Map<String, String> params);

    int downloadRequest(Map<String, Object> params) throws Exception;
}
