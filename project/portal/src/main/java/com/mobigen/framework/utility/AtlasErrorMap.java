package com.mobigen.framework.utility;

import java.util.HashMap;
import java.util.Map;

public class AtlasErrorMap {

    public static String getErrorMessage(String errorMessage) {
        Map<String, String> errorMap = AtlasErrorMap.parseAtlasErrorMessage(errorMessage);
        return errorMap.get("errorMessage");
    }

    private static Map parseAtlasErrorMessage(String errorMessage) {
        Map<String, String> resultMap = new HashMap<String, String>();

        try {
            String parsedMessage = errorMessage.substring(
                    errorMessage.indexOf("errorMessage") + 15,
                    errorMessage.lastIndexOf("}") - 1
            );

            resultMap.put("errorMessage", parsedMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error on Parsing Atlas API execution error string");

            /*
            카테고리 추가 시 에러메시지 유형
                400 Bad Request: "{"errorCode":"ATLAS-400-00-019","errorMessage":"test_category2-2: invalid name for CLASSIFICATION.  Names must consist of a letter followed by a sequence of letter, number, space, or _ characters"}"
            */
        }

        return resultMap;
    }
}
