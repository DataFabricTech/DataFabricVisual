package com.mobigen.datalake.system.role;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin/system/role")
@Controller
@AllArgsConstructor
public class RoleManagementControler {
    private final RoleManagementService roleManagementService;
  
    /**
     * 권한 관리 - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/search")
    public Object getRoleManagementSearchList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord, @RequestParam("roleType") String roleType)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        param.put("roleType", roleType);
        
        total = roleManagementService.getRoleManagementSearchListCount(param);
        list = (List<Map>) roleManagementService.getRoleManagementSearchList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    /**
     * 권한 관리 - 삭제
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/delete")
    @ResponseBody
    public Object deleteRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            // TO-DO : 로직 검토 필요
            int roleUserCount = 0;
            roleUserCount = roleManagementService.getRoleUserCount(param);
            
            if (roleUserCount > 0) {
                String errorMessage = "해당되는 권한에 사용자가 있습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);

                return result;
            }

            // 권한 타입이 데이터 권한인 경우에만 동기화
            List<Map> searchList = roleManagementService.searchRoleItem(param);

            // 
            int count = 0;
            count = roleManagementService.deleteRoleItem(param);
            
            if (searchList.size() > 0 && searchList.get(0).get("role_type").toString().equals("0")) {
                result.put("errorMessage", "");
                result.put("isFailed", false);
                
                return result;
            }

            // ES 동기화 처리
            String[] roleIds = {param.get("role_id").toString()};
            boolean roleEsUpdateResult = roleManagementService.essyncRolesUpdates(Arrays.asList(roleIds), "delete");

            if(!roleEsUpdateResult) {
                String errorMessage = "요청을 처리하는 중 에러가 발생하였습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 권한 관리 - 등록
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/register")
    @ResponseBody
    public Object registerRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List list = roleManagementService.searchRoleItem(param);

            if (list.size() > 0 && !"Y".equals(param.get("updateFlag"))) {
                String errorMessage = "이미 등록된 권한입니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);

                return result;
            } else {
                int count = 0;
                count = roleManagementService.registerRoleItem(param);

                if (count <= 0){
                    String errorMessage = "등록에 실패하였습니다.";
                    result.put("errorMessage", errorMessage);
                    result.put("isFailed", true);
                }
            }

            if ("Y".equals(param.get("updateFlag"))) {
                // ES 동기화 처리
                String[] roleIds = {param.get("role_id").toString()};
                boolean roleEsUpdateResult = roleManagementService.essyncRolesUpdates(Arrays.asList(roleIds), "update");

                if(!roleEsUpdateResult) {
                    String errorMessage = "요청을 처리하는 중 에러가 발생하였습니다.";
                    result.put("errorMessage", errorMessage);
                    result.put("isFailed", true);
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 권한 관리 - 등록 - 중복 체크
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/checkDupRoleId")
    @ResponseBody
    public Object checkDupRoleId(@RequestParam("role_id") String role_id)  throws Exception {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("role_id", role_id);

            List list = roleManagementService.searchRoleItem(param);
            if (list.size() > 0) {
                result.put("register_result", "N");
                result.put("register_result_message", "중복되는 권한입니다.");
            } else {
                result.put("register_result", "Y");
            }
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 권한 관리 - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/searchDetail")
    public Object getRoleDetailSearchList(@RequestParam("roleId") String roleId)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("roleId", roleId);

        list = (List<Map>) roleManagementService.getRoleDetailSearchList(param);
           
        result.put("list", list);
        return result;
    }

    /**
     * 사용자 권한 관리 - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/userSearch")
    public Object getUserRollList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord, @RequestParam("role_id") String roleId)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        param.put("role_id", roleId);

        total = roleManagementService.getUserRoleSearchListCount(param);
        list = (List<Map>) roleManagementService.getUserRoleSearchList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    /**
     * 부서 권한 관리 - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/deptSearch")
    public Object getDeptRollList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord, @RequestParam("role_id") String roleId)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        param.put("role_id", roleId);

        total = roleManagementService.getDeptRoleSearchListCount(param);
        list = (List<Map>) roleManagementService.getDeptRoleSearchList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    /**
     * 데이타 권한 관리 - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/metaSearch")
    public Object getMetaRollList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord, @RequestParam("role_id") String roleId)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        param.put("role_id", roleId);

        total = roleManagementService.getMetaRoleSearchListCount(param);
        list = (List<Map>) roleManagementService.getMetaRoleSearchList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }
    
    /**
     * 사용자 권한 관리 - 등록
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/userRollSave")
    @ResponseBody
    public Object userRollSave(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            int count = 0;
            count = roleManagementService.registerUserRoleItem(list);

            if (count <= 0){
                String errorMessage = "등록에 실패하였습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 사용자 권한 관리 - 삭제
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteUserRoleItem")
    @ResponseBody
    public Object deleteUserRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            int count = 0;
            count = roleManagementService.deleteUserRoleItem(list);

            if (count <= 0){
                String errorMessage = "삭제 처리 실패했습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }
    
    /**
     * 조직 권한 관리 - 등록
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deptRollSave")
    @ResponseBody
    public Object registerDeptRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            int count = 0;
            count = roleManagementService.registerDeptRoleItem(list);

            if (count <= 0){
                String errorMessage = "등록에 실패하였습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 조직 권한 관리 - 삭제
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteDeptRoleItem")
    @ResponseBody
    public Object deleteDeptRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            int count = 0;
            count = roleManagementService.deleteDeptRoleItem(list);

            if (count <= 0){
                String errorMessage = "삭제 처리 실패했습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 데이터 권한 관리 - 등록
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/metaRollSave")
    @ResponseBody
    public Object registerMetaRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            
            int count = 0;
            count = roleManagementService.registerMetaRoleItem(list);

            if (count <= 0){
                String errorMessage = "등록에 실패하였습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }


            if (list.isEmpty()) {
                String errorMessage = "등록할 데이타가 없습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);

                return result;
            }

            if (list.size() > 0 && list.get(0).containsKey("role_id")) {
                // ES 동기화 처리
                String[] roleIds = {list.get(0).get("role_id").toString()};
                boolean roleEsUpdateResult = roleManagementService.essyncRolesUpdates(Arrays.asList(roleIds), "update");

                if(!roleEsUpdateResult) {
                    String errorMessage = "요청을 처리하는 중 에러가 발생하였습니다.";
                    result.put("errorMessage", errorMessage);
                    result.put("isFailed", true);
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 데이터 권한 관리 - 삭제
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteMetaRoleItem")
    @ResponseBody
    public Object deleteMetaRoleItem(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            if (list.isEmpty()) {
                String errorMessage = "삭제할 데이타가 없습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);

                return result;
            }

            if (list.size() > 0 && list.get(0).containsKey("role_id")) {
                // 삭제전에 ES 동기화 처리
                String[] roleIds = {list.get(0).get("role_id").toString()};
                boolean roleEsUpdateResult = roleManagementService.essyncRolesUpdates(Arrays.asList(roleIds), "delete");

                if(!roleEsUpdateResult) {
                    String errorMessage = "요청을 처리하는 중 에러가 발생하였습니다.";
                    result.put("errorMessage", errorMessage);
                    result.put("isFailed", true);
                }
            }

            int count = 0;
            count = roleManagementService.deleteMetaRoleItem(list);

            if (count <= 0){
                String errorMessage = "삭제 처리 실패했습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    /**
     * 데이타 권한 조회 - 조회
    *
    * @params Object       카테고리명
    * @return Object       메타 데이터 통합 관리 대상 목록 반환
    */
    @ResponseJsonResult
    @PostMapping("/metaDatalist")
    public Object getMetaDataList(@RequestBody Map<String, Object> param) throws Exception {
        return roleManagementService.getMetaDataManageList(param);
    }

    /**
     * 데이타 권한 조회(상세) - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/metaDataInfo")
    public Object getMetaDataInfo(@RequestParam("metaId") String metaId)  throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("metaId", metaId);

        return roleManagementService.getMetaDataInfo(param);
    }

    /**
     * 데이타 권한 조회(상세) - 리스트 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/metaDataSearch")
    public Object getMetaDataList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord, @RequestParam("metaId") String metaId)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        param.put("meta_id", metaId);

        total = roleManagementService.getMetaDataListCount(param);
        list = (List<Map>) roleManagementService.getMetaDataList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    /**
     * 사용자 권한 조회 - 리스트 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/userDataSearch")
    public Object userDataSearch(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        total = roleManagementService.getUserDataListCount(param);
        list = (List<Map>) roleManagementService.getUserDataList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    /**
     * 사용자 권한 조회(상세) - 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/userDataInfo")
    public Object getUserDataInfo(@RequestParam("userId") String userId)  throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);

        return roleManagementService.getUserDataInfo(param);
    }

    /**
     * 사용자 권한 조회(상세) - 리스트 조회
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/userMetaDataSearch")
    public Object userMetaDataSearch(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord, @RequestParam("userId") String userId)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        int total = 0;
        List list = new ArrayList<Map>();

        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));
        param.put("userId", userId);

        total = roleManagementService.getUserMetaDataListCount(param);
        list = (List<Map>) roleManagementService.getUserMetaDataList(param);
           
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    /**
     * 권한 관리 - es 동기화 호출
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/esSyncCall")
    public Object esSyncCall(@RequestBody Map<String, Object> param)  throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        if(param.get("role_id") != null) {
            String[] roleIds = {param.get("role_id").toString()};
            boolean roleEsUpdateResult = roleManagementService.essyncRolesUpdates(Arrays.asList(roleIds), "update");
            
            if (!roleEsUpdateResult) {
                String errorMessage = "요청을 처리하는 중 에러가 발생하였습니다.";
                result.put("errorMessage", errorMessage);
                result.put("isFailed", true);
            } else {
                result.put("isFailed", false);
            }
        }

        return result;
    }

}
