package com.mobigen.datalake.system.collector;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
}
