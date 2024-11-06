package com.mobigen.datalake.system.user.group;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin/manager")
@Controller
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @ResponseJsonResult
    @GetMapping("/group")
    public Object getGroup(@RequestParam("unum") String unum, @RequestParam("type") String type, @RequestParam("word") String word) throws Exception {
        HashMap result = new HashMap();
        PageHelper.startPage(Integer.parseInt(unum), 2);
        PageInfo info = PageInfo.of(groupService.getAllGroup(type, word, "0"));
        result.put("page", info);
        result.put("user", groupService.getGroupUser(type, word, info.getList()));

        return result;
    }

    @ResponseJsonResult
    @PostMapping("/group/add")
    @ResponseBody
    public Object postNewGroup(@RequestBody Map<String, Object> param) throws Exception {
        String groupName = String.valueOf(param.get("groupName"));
        String groupDesc = String.valueOf(param.get("groupDesc"));
        List<Map> userList = (List<Map>) param.get("userList");
        return groupService.addNewGroup(groupName, groupDesc, userList);
    }

    @ResponseJsonResult
    @PostMapping("/group/update")
    @ResponseBody
    public Object postUpdateGroup(@RequestBody Map<String, Object> param) throws Exception {
        String groupName = String.valueOf(param.get("groupName"));
        String groupDesc = String.valueOf(param.get("groupDesc"));
        List<Map> userList = (List<Map>) param.get("userList");
        List<Map> blackList = (List<Map>) param.get("blackList");
        return groupService.updateGroup(groupName, groupDesc, userList, blackList);
    }

    @ResponseJsonResult
    @PostMapping("/group/delete")
    public Object deleteGroup(@RequestBody Map<String, List> param) throws Exception {
        List<Map> groupList = param.get("groupList");
        return groupService.deleteGroup(groupList);
    }

}
