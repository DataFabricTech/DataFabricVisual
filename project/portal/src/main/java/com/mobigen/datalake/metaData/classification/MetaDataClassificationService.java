package com.mobigen.datalake.metaData.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mobigen.datalake.atlas.AtlasRestAPI;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.utility.AtlasErrorMap;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MetaDataClassificationService {

    private final MetaDataClassificationMapper metaDataClassificationMapper;
    private final AtlasRestAPI atlasRestAPI;

    private final String BASE_URL = "/types/typedefs?type=classification";

    // 카테고리 담당부서 조회
    public Object getClassificationDept(String name) throws Exception {
        return metaDataClassificationMapper.getClassificationDept(name);
    }

    // 카테고리 Depth 조회
    public Object getClassificationDepth(String name) throws Exception {
        return metaDataClassificationMapper.getClassificationDepth(name);
    }

    // 하위 카테고리 추가
    public Object insertClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Boolean isFailed = false;

        Map atlasParam = (Map) params.get("forAtlas"); // Atlas API 호출 시 사용 파라미터
        Map tableParam = (Map) params.get("forRDB"); // <table> insert 시 사용 파라미터

        JsonResult atlasResult = atlasRestAPI.post(BASE_URL, atlasParam);
        if (atlasResult.getResult() > 0) {
            // Atlas 추가 성공 시 테이블에도 추가
            metaDataClassificationMapper.insertClassification(tableParam);

            metaDataClassificationMapper.createClassificationCount(tableParam);
        } else {
            // 삭제 불가 시 Atlas 에러 메시지 리턴
            result.put("errorMessage", AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage()));
            isFailed = true;
        }

        result.put("isFailed", isFailed);
        return result;
    }

    // 카테고리 수정
    public Object updateClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Boolean isFailed = false;

        Map atlasParam = (Map) params.get("forAtlas"); // Atlas API 호출 시 사용 파라미터
        Map tableParam = (Map) params.get("forRDB"); // <table> insert 시 사용 파라미터

        JsonResult atlasResult = atlasRestAPI.put(BASE_URL, atlasParam, false);
        if (atlasResult.getResult() > 0) {
            // Atlas 수정 성공 시 테이블에도 추가
            metaDataClassificationMapper.updateClassification(tableParam);
        } else {
            // 삭제 불가 시 Atlas 에러 메시지 리턴
            result.put("errorMessage", AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage()));
            isFailed = true;
        }

        result.put("isFailed", isFailed);
        return result;
    }

    // 카테고리 삭제
    public Object deleteClassification(String name) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Boolean isFailed = false;

        String URL = "/types/typedef/name/" + name;

        // Atlas API 호출, 삭제 불가 시 Atlas 에러 메시지를 그대로 사용자에게 리턴
        JsonResult atlasResult = atlasRestAPI.delete(URL, null, false);
        if (atlasResult.getResult() > 0) {
            // Atlas 삭제 성공 시 리턴값 없음. 테이블에서도 삭제
            metaDataClassificationMapper.deleteClassification(name);
        } else {
            // 삭제 불가 시 Atlas 에러 메시지 리턴
            String errorMessage = AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage());

            // 카테고리를 참조하는 경우
            if (errorMessage.indexOf("Given type " + name + " has references") != -1 || (errorMessage.startsWith("Given type") && errorMessage.endsWith(" has references"))) {
                result.put("errorMessage", name + " 카테고리를 참조하고 있는 개체가 있습니다.");
            } else {
                result.put("errorMessage", errorMessage);
            }
            isFailed = true;
        }

        result.put("isFailed", isFailed);
        return result;
    }

    // 카테고리 맵핑
    public Object mapClassification(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Boolean isFailed = false;

        // Map entity = (Map) params.get("entity");
        List<Map> entities = (List) params.get("entities");
        // List entityGuids = (List) entity.get("entityGuids");
        // int entityGuidsCount = entityGuids.size();
        List superTypes = (List) params.get("superTypes");

        // 테이블 유형일 경우 하위 컬럼 GUID 목록 추출하여 함께 카테고리 맵핑
        List<String> tableTypes = (List<String>) params.get("tableTypes");
        // if (tableTypes.size() > 0) {
        //     List<String> columnGuids = new ArrayList<>();

        //     for (String tableTypeGuid : tableTypes) {
        //         columnGuids.addAll(this.getColumns(tableTypeGuid));
        //     }

        //     entityGuids.addAll(columnGuids);
        // }

        // 선택된 엔티티 맵핑
        String URL = "/entity/bulk/classification";

        for (Map entity : entities){
            List entityGuids = (List) entity.get("entityGuids");
            int entityGuidsCount = entityGuids.size();

            if (tableTypes.size() > 0) {
                List<String> columnGuids = new ArrayList<>();
    
                for (String tableTypeGuid : tableTypes) {
                    columnGuids.addAll(this.getColumns(tableTypeGuid));
                }
    
                entityGuids.addAll(columnGuids);
            }

            JsonResult atlasResult = atlasRestAPI.post(URL, entity, false);

            if (atlasResult.getResult() > 0) {
                // Atlas 매핑 성공 시 count증가
                // 한번에 여러개 매핑시 count 증가분을 1이 아닌 guid 개수만큼
                
                metaDataClassificationMapper.mapperCountMulti(entity, entityGuidsCount);
            }

            if (atlasResult.getResult() == 0) {
                // 이미 맵핑된 카테고리일 경우 화면에 에러 메시지 출력하지 않음
                // 기맵핑된 카테고리 선택 시 에러 메시지 예시) invalid parameters: entity:
                // 4e586f8d-820d-4216-850a-c5f638bdfa00, already associated with classification:
                // AS
                String errorMessage = AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage());
                if (errorMessage.indexOf("already associated with classification") == -1) {
                    result.put("errorMessage", errorMessage);
                    isFailed = true;
                }
            }
        }




        // 이후는 old code
        // JsonResult atlasResult = atlasRestAPI.post(URL, entity, false);

        // if (atlasResult.getResult() > 0) {
        //     // Atlas 매핑 성공 시 count증가
        //     // 한번에 여러개 매핑시 count 증가분을 1이 아닌 guid 개수만큼
            
        //     metaDataClassificationMapper.mapperCountMulti(entity, entityGuidsCount);
        // }
        // if (atlasResult.getResult() == 0) {
        //     // 이미 맵핑된 카테고리일 경우 화면에 에러 메시지 출력하지 않음
        //     // 기맵핑된 카테고리 선택 시 에러 메시지 예시) invalid parameters: entity:
        //     // 4e586f8d-820d-4216-850a-c5f638bdfa00, already associated with classification:
        //     // AS
        //     String errorMessage = AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage());
        //     if (errorMessage.indexOf("already associated with classification") == -1) {
        //         result.put("errorMessage", errorMessage);
        //         isFailed = true;
        //     }
        // } else {
        //     // 맵핑 성공 시 상위 카테고리 맵핑 대상 확인 후 처리
        //     if (superTypes.size() > 0) {
        //         Map superTypeMapResult = this.mapSuperTypeClassification(entityGuids, superTypes);
        //         Boolean superTypeMapFailed = (Boolean) superTypeMapResult.get("isFailed");
        //         if (superTypeMapFailed) {
        //             // 이미 맵핑된 카테고리일 경우 화면에 에러 메시지 출력하지 않음
        //             String superErrorMessage = (String) superTypeMapResult.get("errorMessage");
        //             if (superErrorMessage.indexOf("already associated with classification") == -1) {
        //                 result.put("errorMessage", superErrorMessage);
        //                 isFailed = true;
        //             }
        //         }
        //     }
        // }

        result.put("isFailed", isFailed);
        return result;
    }

    // 상위 카테고리 맵핑
    public Map mapSuperTypeClassification(List guids, List superTypes) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Boolean isFailed = false;        

        for (int i = 0; i < guids.size(); i++) {
            String URL = "/entity/guid/" + guids.get(i) + "/classifications";
            JsonResult atlasResult = atlasRestAPI.post(URL, superTypes, false);

            if (atlasResult.getResult() == 0) {
                result.put("errorMessage", AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage()));
                isFailed = true;                
                break;
            }
        }

        for (int j = 0; j < superTypes.size(); j++) {
            Map<String, Object> superType = (Map<String, Object>)superTypes.get(j);
            String typeName = superType.get("typeName").toString();

            // 상위 카테고리 매핑시 에러 없으면 카운트 증가
            // 상위 카테고리 매핑시 에러 있고, 카테고리 이름으로 있으면 중복이라 판단하고, 없을 경우만 증가
            if( !result.containsKey("errorMessage") )
                metaDataClassificationMapper.supperMapperCount(superType);
            else if(result.containsKey("errorMessage") && !result.get("errorMessage").toString().contains(typeName))
                metaDataClassificationMapper.supperMapperCount(superType);
        }

        result.put("isFailed", isFailed);
        return result;
    }

    // 카테고리 맵핑 제거
    public Object deleteMapClassification(String name, Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>() {
            {
                put("isFailed", false);
            }
        };

        List<String> targetGuids = new ArrayList<>(); // 카테고리 맵핑 제거 대상 guid 목록
        String guid = (String) params.get("guid"); // 사용자가 선택한 엔티티 guid
        targetGuids.add(guid);

        Boolean isTableType = (Boolean) params.get("isTableType");
        if (isTableType) {
            // 테이블 유형일 경우 하위 컬럼 목록 조회 및 추가
            targetGuids.addAll(this.getColumns(guid));
        } else {
            // 컬럼 유형일 경우 현재 엔티티가 속한 테이블 & 해당 테이블의 하위 컬럼(현재 엔티티 제외) 목록 조회 및 추가
            targetGuids.addAll(this.getTableWithOtherColumns(guid));
        }

        for (String targetGuid : targetGuids) {
            String DEFAULT_URL = "/entity/guid/";
            String url = DEFAULT_URL + targetGuid + "/classification/" + name;
            JsonResult atlasResult = atlasRestAPI.delete(url, null, false);
            if (atlasResult.getResult() == 0) {
                String errorMessage = AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage());
                // 매핑이 이미 없는 경우도 오류로 처리하지 않음
                if (errorMessage.indexOf("No classifications associated with entity: " + targetGuid) == 0) {
                    result.put("isFailed", false);
                    result.put("errorMessage", AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage()));
                } else if (errorMessage.indexOf("Classification " + name + " is not associated with entity") == 0) {
                    result.put("isFailed", false);
                    result.put("errorMessage", AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage()));
                } else {
                    result.put("isFailed", true);
                    result.put("errorMessage", AtlasErrorMap.getErrorMessage(atlasResult.getErrorMessage()));
                    break;
                }
            }
        }
        metaDataClassificationMapper.mapperCountDown(name);

        return result;
    }

    // 하위 컬럼 guid 목록 추출
    private List<String> getColumns(String guid) {
        List<String> guids = new ArrayList<>();

        // guid 기준 상세 조회
        JsonResult detail = this.atlasRestAPI.get("/entity/guid/" + guid);
        if (detail.getResult() > 0) {
            Map data = (Map) detail.getData();
            Object columnList = this.findNestedMapValue(data, "entity/attributes/columns");
            if (columnList instanceof List) {
                for (Map item : (List<Map>) columnList) {
                    String columnGuid = (String) item.get("guid");
                    guids.add(columnGuid);
                }
            }
        }

        return guids;
    }

    // 테이블 guid 및 선택 컬럼을 제외한 나머지 하위 컬럼 guid 목록 추출
    private List<String> getTableWithOtherColumns(String guid) {
        List<String> guids = new ArrayList<>();

        JsonResult detail = this.atlasRestAPI.get("/entity/guid/" + guid);
        if (detail.getResult() > 0) {
            Map data = (Map) detail.getData();
            Object tableGuid = this.findNestedMapValue(data, "entity/attributes/table/guid");

            if (tableGuid != null && tableGuid instanceof String) {
                guids.add((String) tableGuid);

                List<String> columns = this.getColumns((String) tableGuid);
                for (String column : columns) {
                    if (!guid.equals(column)) {
                        guids.add(column);
                    }
                }
            }
        }

        return guids;
    }

    // 다중 중첩된 Map에서 필요한 값 추출
    private Object findNestedMapValue(Map map, String props) {
        Object value = map;
        String[] keys = props.split("/");

        for (int i = 0; i < keys.length; i++) {
            if (value == null) {
                break;
            }

            String key = keys[i];
            Object curVal = ((Map) value).get(key);
            if (curVal != null) {
                value = curVal;
            } else {
                value = null;
            }
        }

        return value;
    }

    // 순서 변경을 위한 카테고리 목록 조회
    public Object getClassificationList(String level) throws Exception {
        List<Map<String, Object>> list = metaDataClassificationMapper.getClassificationList(level);
        return list;
    }

    // 순서 변경 처리
    public Object updateClassificationOrdNo(List<Map<String, Object>> params) throws Exception {
        int updateResult = 0;

        for (Map<String, Object> param : params) {
            updateResult += metaDataClassificationMapper.updateClassificationOrdNo(param);
        }

        return updateResult;
    }
}
