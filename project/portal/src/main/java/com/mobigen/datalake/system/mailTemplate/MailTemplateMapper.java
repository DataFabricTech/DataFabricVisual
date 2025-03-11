package com.mobigen.datalake.system.mailTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Repository("mailTemplate")
public interface MailTemplateMapper {
    List getMailTemplateList(Map<String, Object> param) throws Exception;

    List getMailTemplateInfo(Map<String, Object> param) throws Exception;

    Integer getMailTemplateListTotCnt(Map<String, Object> param) throws Exception;

    Integer saveMailTemplate(@RequestBody Map<String, Object> param) throws Exception;

    Integer deleteMailTemplate(@RequestBody Map<String, Object> param) throws Exception;
}