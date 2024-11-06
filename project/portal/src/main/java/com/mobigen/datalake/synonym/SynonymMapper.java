package com.mobigen.datalake.synonym;

import java.util.List;
import java.util.Map;

public interface SynonymMapper {
    List getDistinctTermList(Map params) throws Exception;
    List getSynonymList(Map params) throws Exception;
    int getSynonymListTotal(Map params) throws Exception;
    List getSynonymListOne(String params) throws Exception;

    int addSynonym(Map params) throws Exception;
    int addSynonymSet(Map<String, Object> params) throws Exception;
    int deleteSynonym(List<Map<String, Object>> params) throws Exception;
    int deleteMySynonym(Map<String, Object> params) throws Exception;
    int modifyTerm(Map<String, Object> params) throws Exception;
    int deleteSynonymSet(List<Map<String, Object>> params) throws Exception;

    int addDeleteRequestInfo(Map params) throws Exception;
    int addDeleteRequestItemInfo(Map params) throws Exception;
    int getRecentDeleteRequestInfoId() throws Exception;

    List getDeleteRequestListAndItem(Map params) throws Exception;
    List getDeleteRequestListAndItemAdmin(Map params) throws Exception;
    List getDeleteRequestListItem() throws Exception;

    int getDeleteRequestOneStatus(String id) throws Exception;
    int deleteRequestOne(long id) throws Exception;
    int deleteRequestOneItem(long id) throws Exception;
    int deleteSynonymOne(Map<String, Object> params) throws Exception;
    int confirmSynonymRequestOne(Map<String, Object> params) throws Exception;
    int rejectSynonymRequestOne(Map<String, Object> params) throws Exception;
    int rejectSynonymRequestAll(Map<String, Object> params) throws Exception;
    int confirmSynonymRequestAll(Map<String, Object> params) throws Exception;
    int deleteSynonymAll(Map<String, Object> params) throws Exception;

}
