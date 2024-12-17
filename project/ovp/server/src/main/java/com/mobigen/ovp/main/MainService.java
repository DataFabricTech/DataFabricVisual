package com.mobigen.ovp.main;

import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.entity.ModelIndex;
import com.mobigen.ovp.model_creation.ModelCreationService;
import com.mobigen.ovp.search.SearchService;
import com.mobigen.ovp.search_detail.SearchDetailService;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import com.mobigen.ovp.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final ModelCreationService modelCreationService;
    private final SearchDetailService searchDetailService;
    private final UserClient userClient;

    private String getDataType(String dataType) {
        String newDataType = null;
        for (ModelIndex index : ModelIndex.values()) {
            if (index.name().equals(dataType)) {
                newDataType = index.name().equals(ModelIndex.table.name()) ? ModelType.TABLES.getValue():  ModelType.STORAGE.getValue();
                return newDataType;
            }
        }
        return newDataType;
    }

    /**
     * 메인 >
     *
     * @return
     * @throws Exception
     */
    public Object getUserFollows(String id) {
//        return modelCreationService.getMyModelList(id, "", "follows", 3);
        // TODO: getMyModelList의 버그로 인한 임시 처리
        List<DataModelDetailResponse> list = new ArrayList<>();
        Map<String, Object> userInfo = userClient.getUserWithFields(id, "follows");
        List<Map<String, Object>> followsList = (List<Map<String, Object>>) userInfo.get("follows");

        for (Map<String, Object> follow : followsList) {
            if (list.size() == 3) {
                break;
            }

            String followId = (String) follow.get("id");
            String dataType = (String) follow.get("type");
            String newDataType = getDataType(dataType);

            if (newDataType == null) {
                continue;
            }

            DataModelDetailResponse dataModelDetail;

            try {
                dataModelDetail = searchDetailService.getDataModelDetail(followId, newDataType);
            } catch (Exception e) {
                continue;
            }

            list.add(dataModelDetail);
        }

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> all = new HashMap();
        all.put("all", list);
        result.put("data", all);

        return list;
    }
}
