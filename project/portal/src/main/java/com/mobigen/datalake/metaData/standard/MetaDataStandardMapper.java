package com.mobigen.datalake.metaData.standard;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("metaDataStandardMapper")
public interface MetaDataStandardMapper {
    List<Map<String, Object>> getMetaDataStandardTermList(Map<String, Object> param) throws Exception;
    Integer getMetaDataStandardTermListCount(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getMetaDataStandardWordList(Map<String, Object> param) throws Exception;
    Integer getMetaDataStandardWordListCount(Map<String, Object> param) throws Exception;

    List<Map<String, Object>> getMetaDataStandardDomainList(Map<String, Object> param) throws Exception;
    Integer getMetaDataStandardDomainListCount(Map<String, Object> param) throws Exception;

    Map<String, Object> getMetaDataStandardTermInfo(String id) throws Exception;
    Map<String, Object> getMetaDataStandardWordInfo(String id) throws Exception;
    Map<String, Object> getMetaDataStandardDomainInfo(String id) throws Exception;

    Integer deleteSoftTerm(String id) throws Exception;
    Integer deleteSoftWord(String id) throws Exception;
    Integer deleteSoftDomain(String id) throws Exception;

    Integer existsByKorName(String word) throws Exception;
    Integer existsByEnglishSign(String word) throws Exception;

    Integer existsByKorNameFromWord(String word) throws Exception;
    Integer existsByEnglishSignFromWord(String word) throws Exception;
    Integer existsByEnglishFromWord(String word) throws Exception;

    Integer existsByKorNameFromDomain(String word) throws Exception;

    Integer dupCheckDomainName(String domain) throws Exception;

    Integer createTerm(Map<String, Object> params) throws Exception;
    Integer createTermDomain(Map<String, Object> params) throws Exception;
    Integer updateTerm(Map<String, Object> params) throws Exception;
    Integer updateTermInfo(Map<String, Object> params) throws Exception;
    Integer updateTermDomain(Map<String, Object> params) throws Exception;
    Integer updateDomainFromTerm(Map<String, Object> params) throws Exception;

    Integer createWord(Map<String, Object> params) throws Exception;
    Integer updateWord(Map<String, Object> params) throws Exception;

    Integer createDomain(Map<String, Object> params) throws Exception;
    Integer updateDomain(Map<String, Object> params) throws Exception;

    Map<String, Object> getDomainInfoByDomainClassifiedName(String domain_classified_name) throws Exception;

    List<Map<String, Object>> getAllTermsForDuplicateCheck() throws Exception;
    List<String> getAllDomainNames() throws Exception;

    Integer insertStandardTerms(List<Map<String, Object>> standardTerms) throws Exception;
}
