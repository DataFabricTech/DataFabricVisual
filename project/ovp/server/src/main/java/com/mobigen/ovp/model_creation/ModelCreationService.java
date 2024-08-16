package com.mobigen.ovp.model_creation;

import com.mobigen.ovp.model_creation.client.DolphinClient;
import com.mobigen.ovp.model_creation.client.ModelCreationClient;
import com.mobigen.ovp.user.UserClient;
import com.mobigen.ovp.user.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelCreationService {
    private final DolphinClient dolphinClient;
    private final ModelCreationClient modelCreationClient;
    private final UserClient userClient;

    public Object executeQuery(Map<String, Object> param) throws Exception {
        Map<String, Object> stringObjectMap = dolphinClient.executeQuery(param);

        Map<String, Object> transformedMap = new HashMap<>();

        List<Map<String, String>> columnDefs = new ArrayList<>();
        List<Map<String, String>> originalColumns = (List<Map<String, String>>) stringObjectMap.get("columns");

        for (Map<String, String> column : originalColumns) {
            Map<String, String> columnDef = new HashMap<>();
            columnDef.put("field", column.get("name"));
            columnDefs.add(columnDef);
        }

        transformedMap.put("columnDefs", columnDefs);

        Map<String, Object> resultData = (Map<String, Object>) stringObjectMap.get("resultData");
        List<String> columnNames = (List<String>) resultData.get("columns");
        List<List<Object>> rows = (List<List<Object>>) resultData.get("rows");
        List<Map<String, Object>> rowData = new ArrayList<>();

        for (List<Object> row : rows) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                rowMap.put(columnNames.get(i), row.get(i));
            }
            rowData.add(rowMap);
        }

        transformedMap.put("totalRows", stringObjectMap.get("totalRows"));
        transformedMap.put("rowData", rowData);

        String startedTime = (String) stringObjectMap.get("startedTime");
        ZonedDateTime zdt = ZonedDateTime.parse(startedTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = zdt.format(formatter);

        transformedMap.put("runTime", stringObjectMap.get("elapsedTime"));
        transformedMap.put("startTime", startTime);

        return transformedMap;
    }

    public Object addBookMark(String id) throws Exception {
        UserInfoDTO userInfo = userClient.getUserInfo();
        String jsonLoginUserId = "\"" + userInfo.getId() + "\"";
        modelCreationClient.addBookMark(id, jsonLoginUserId);
        return null;
    }

    public Object removeBookMark(String id) throws Exception {
        UserInfoDTO userInfo = userClient.getUserInfo();
        modelCreationClient.deleteBookMark(id, userInfo.getId());
        return null;
    }
}
