package com.mobigen.ovp.main;

import com.mobigen.ovp.search_detail.SearchDetailService;
import com.mobigen.ovp.search_detail.dto.response.DataModelDetailResponse;
import com.mobigen.ovp.user.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final SearchDetailService searchDetailService;
    private final UserClient userClient;

    /**
     * 메인 >
     *
     * @return
     * @throws Exception
     */
    public Object getUserFollows(String id) throws Exception {
        Map<String, Object> userInfo = userClient.getUserFollows(id, "follows");
        List<Map<String, Object>> followsList = (List<Map<String, Object>>) userInfo.get("follows");
        List<String> followsIdList = new ArrayList<>();
        List<DataModelDetailResponse> AllResult = new ArrayList<>();
        List<DataModelDetailResponse> result = new ArrayList<>();

        for (Map<String, Object> follow : followsList) {
            String followId = (String) follow.get("id");
            followsIdList.add(followId);
        }

        for (String followId : followsIdList) {
            DataModelDetailResponse dataModelDetail = searchDetailService.getDataModelDetail(followId);
            AllResult.add(dataModelDetail);
        }

        result.add(AllResult.get(0));
        result.add(AllResult.get(1));
        result.add(AllResult.get(2));

        return result;
    }
}
