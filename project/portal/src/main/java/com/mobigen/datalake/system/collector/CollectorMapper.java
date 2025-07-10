package com.mobigen.datalake.system.collector;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("CollectorMapper")
public interface CollectorMapper {
  List<Map<String, Object>> getCollectorList(Map<String, Object> params) throws Exception;

  int getCollectorListTotal(Map<String, Object> params) throws Exception;

  int systemIdDupCheck(String system_id) throws Exception;

  int addCollector(Map<String, Object> params) throws Exception;

  int updateCollector(Map<String, Object> params) throws Exception;

  int updateCollectorNoPassword(Map<String, Object> params) throws Exception;

  int deleteCollector(String system_id) throws Exception;

  List<String> getSystemTypes() throws Exception;

  Map<String, Object> getCollectorOne(String system_id) throws Exception;

  List<Map<String, Object>> getFileInfoList(String system_id) throws Exception;

  List<Map<String, Object>> getCollectorFileInfoList(String system_id) throws Exception;

  List<Map<String, Object>> getMinioCodeInfo() throws Exception;
  
  Map<String, Object> getMinioSystemInfo(String system_name, String system_type) throws Exception;
}
