package com.mobigen.datalake.metaData.manage;

import com.mobigen.framework.result.JsonResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class MetaDataManageService {

    private final MetaDataManageMapper metaDataManageMapper;


    // 메타 데이터 통합 관리 대상 목록 조회
    public Map<String, Object> getMetaDataManageList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        totCnt = metaDataManageMapper.getMetaDataManageListTotCnt(params);
        resultList = metaDataManageMapper.getMetaDataManageList(params);

        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    public List<Map<String, Object>> getMappedMetaDataManageListByClassificationId(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.getMappedMetaDataManageListByClassificationId(params);
    };

    public List<Map<String, Object>> getMetaDataListByParentId(String parentId) throws Exception {
        return metaDataManageMapper.getMetaDataListByParentId(parentId);
    }

    public int updateMetaDataClassification(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.updateMetaDataClassification(params);
    }

    public int deleteMetaDataClassification(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.deleteMetaDataClassification(params);
    }

    public List<String> getFilterOptions() throws Exception {
        return metaDataManageMapper.getFilterOptions();
    }
}
