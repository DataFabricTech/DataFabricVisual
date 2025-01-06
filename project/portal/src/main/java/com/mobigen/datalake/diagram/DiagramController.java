package com.mobigen.datalake.diagram;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobigen.datalake.metaData.manage.MetaDataManageService;
import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.result.annotation.ResponseJsonResult;

@Slf4j
@RequestMapping("/api")
@Controller
@AllArgsConstructor
public class DiagramController {
    private final DiagramService diagramService;
    private final MetaDataManageService metaDataManageService;

    /**
     *
     * @param direction all / parent / child 중 하나가 들어감 해당 노드 부터의 방향을 나타냄
     * @param nodeId 기준이 되는 노드 정보
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/node/{direction}/{nodeId}")
    public JsonResult getNodeRelation(@PathVariable String direction, @PathVariable String nodeId) throws Exception {
        JsonResult result = new JsonResult();
        result.setResult(JsonResult.RESULT_SUCCESS);
        result.setData(diagramService.getNodeRelation(direction, nodeId));
        return result;
    }

    @ResponseBody
    @GetMapping("/node/check")
    public Boolean checkDuplicate(@RequestParam String data) {
        return diagramService.checkDuplicate(data);
    }

    @ResponseJsonResult
    @GetMapping("/message")
    public Object message() {
        return diagramService.getMessage();
    }

    @ResponseJsonResult
    @GetMapping("/sample-images")
    public Object getSampleImages(int count) throws Exception {
        return diagramService.getImages(count);
    }

    @ResponseJsonResult
    @GetMapping("/csrf-token")
    public Object getCsrfToken() throws Exception {
        return true;
    }

    /**
     * 데이터 흐름 수정 모달 조회
     *
     * @param guid 조회 대상 guid
     * @return Object node link 정보 목록 반환
     */
    @ResponseJsonResult
    @GetMapping("/node/{guid}")
    public Object getNodeLinksInfo(@PathVariable String guid) throws Exception {
        return diagramService.getNodeLinksInfo(guid);
    }

    /**
     * 데이터 흐름 수정 모달 툴팁 조회
     *
     * @param guid 조회 대상 guid
     * @return Object 툴팁을 위한 메타 정보 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/node/tooltip")
    public Object getNodeInfoTooltip(@RequestBody Map<String, Object> params) throws Exception {
        return diagramService.getMetaDataForTooltipOne(params);
    }

    /**
     * 데이터 흐름 수정 모달 조회
     *
     * @param guid 조회 대상 guid
     * @return Object node link 정보 목록 반환
     */
    @ResponseJsonResult
    @GetMapping("/node/detail/tooltip/{guid}")
    public Object getNodeInfo(@PathVariable String guid) throws Exception {
        return diagramService.getNodeInfo(guid);
    }

    /**
     * 데이터 흐름 수정
     *
     * @param params 수정 대상 node link 목록
     * @return Object node link 정보 목록 반환
     */
    @ResponseJsonResult
    @PostMapping("/node")
    @ResponseBody
    public Object saveNodeLinksInfo(
            @RequestBody List<Map<String, Object>> params) throws Exception {
        JsonResult result = new JsonResult();

        int executed = diagramService.saveNodeLinksInfo(params);
        if (executed > 0) {
            result.setResult(JsonResult.RESULT_SUCCESS);
        } else {
            result.setResult(JsonResult.RESULT_FAIL);
        }

        return result;
    }

    /**
     * 데이터 흐름 삭제
     *
     * @param linkID 삭제 대상 link ID
     * @return Object 삭제 실행 결과
     */
    @DeleteMapping("/node/{linkID}")
    @ResponseBody
    public Object deleteNodeLink(@PathVariable int linkID) throws Exception {       
        return diagramService.deleteNodeLink(linkID);
    }

    /**
     * @param params 수정 대상 node link 목록
     * @return
     */
    @PostMapping("/node/insert")
    @ResponseBody
    public Object saveNode(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> tooltipMap = (Map<String, Object>) params.get("tooltip");
        Object idObj = tooltipMap.get("id");
        String id = (String) idObj;
        String db_name = (String) tooltipMap.get("system_type");
        tooltipMap.put("dbName", db_name);
        if (diagramService.checkDuplicate(id)) {
            diagramService.updateSelfNode(tooltipMap);
        } else {
            diagramService.saveSelfNode(tooltipMap);
        }          
        return diagramService.saveNodeLink(params);

    }

    /**
     *
     * @param params
     * @return Object
     */
    @ResponseJsonResult
    @PutMapping("/node/add/info")
    @ResponseBody
    public Object saveNodeInfo(@RequestBody List<Map<String, Object>> params) throws Exception {
        JsonResult result = new JsonResult();

        int executed = diagramService.saveNodeInfo(params);
        if (executed > 0) {
            result.setResult(JsonResult.RESULT_SUCCESS);
        } else {
            result.setResult(JsonResult.RESULT_FAIL);
        }

        return result;
    }

    /**
     *
     * @param params
     * @return Object
     */
    @ResponseJsonResult
    @PutMapping("/node/self")
    @ResponseBody
    public Object saveSelfNodeInfo(@RequestBody Map<String, Object> params) throws Exception {
        JsonResult result = new JsonResult();
        int executed = diagramService.saveSelfNode(params);
        if (executed > 0) {
            result.setResult(JsonResult.RESULT_SUCCESS);
        } else {
            result.setResult(JsonResult.RESULT_FAIL);
        }

        return result;
    }

    /**
     *
     * @param params
     * @return Object
     */
    @ResponseJsonResult
    @PatchMapping("/node/self/update")
    @ResponseBody
    public Object updateSelfNodeInfo(@RequestBody Map<String, Object> params) throws Exception {
        JsonResult result = new JsonResult();
        int executed = diagramService.updateSelfNode(params);
        if (executed > 0) {
            result.setResult(JsonResult.RESULT_SUCCESS);
        } else {
            result.setResult(JsonResult.RESULT_FAIL);
        }

        return result;
    }

    @ResponseJsonResult
    @PostMapping("/node/search")
    public Object getMetaDataList(@RequestBody Map<String, Object> params) throws Exception {
        return diagramService.getMetaDataList(params);
    }

    @ResponseJsonResult
    @PostMapping("/node/getMetaDataForTooltip")
    public Object getMetaDataForTooltip(@RequestBody List<String> guidList) throws Exception {
        List<Map<String, Object>> metaDataInfo = diagramService.getMetaDataForTooltip(guidList);
        return metaDataInfo;
    }

    @ResponseJsonResult
    @GetMapping("/node/getDbList")
    public Object getDbList() throws Exception {
        return diagramService.getDbList();
    }
}
