package com.mobigen.datalake.system.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
@RequestMapping("/api/admin/manager")
@Controller
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 사용자 목록 조회
     * @param pageSize
     * @param pnum
     * @param searchType
     * @param searchWord
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/user/list")
    public Object getUserList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum, @RequestParam("searchType") String searchType, @RequestParam("searchWord") String searchWord) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);

        int total = 0;
        List list = new ArrayList<Map>();

        // 검색 조건이 권한일 경우 다음 단어
        if (!searchWord.equals("") && searchType.equals("role_name")) {
            
            Map<String, String> wordMap = new HashMap<String, String>();
            wordMap.put("사용자", "User");
            wordMap.put("시스템 관리자", "Super-Admin");
            wordMap.put("데이터 엔지니어", "Manager");

            List<String> searchWordList = new ArrayList<>();
            for (Entry<String, String> word : wordMap.entrySet()) {
                if (word.getKey().contains(searchWord)) {
                    searchWordList.add(word.getValue());
                }
            }

            param.put("limit", String.valueOf(pageSize));
            param.put("offset", String.valueOf(pageSize * (pnum - 1)));

            if (searchWordList.size() > 0) {
                param.put("searchWordList", searchWordList);
                total = adminService.getUserListTotalWithRoleName(param);
                list = adminService.getUserListWithRoleName(param);
            }

        } else {

            param.put("searchWord", searchWord);
            param.put("limit", String.valueOf(pageSize));
            param.put("offset", String.valueOf(pageSize * (pnum - 1)));
    
            total = adminService.getUserListTotal(param);
            
            list = adminService.getUserList(param);
            
        }

        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;            

    }

    /**
     * 사용자 단건 조회
     * @param user_id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/user/one")
    public Object getUser( @RequestParam("user_id") String user_id) throws Exception {
        return adminService.getUser(user_id);
    }

    /**
     * 사용자 정보 수정 (권한)
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/user/update")
    @ResponseBody
    public Object updateUserInfo(@RequestBody Map<String, String> param) throws Exception {
        return adminService.updateUserInfo(param);
    }

    /**
     * 장기 미접속 해제
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/user/UnlockLoginDeactivation")
    @ResponseBody
    public Object unlockLoginDeactivation(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
        return adminService.unlockLoginDeactivation(param, request);
    }
}
