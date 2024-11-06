package com.mobigen.datalake.system.user.group;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupMapper groupMapper;

    public List getAllGroup(String searchType, String word, String groupID) throws Exception {
        List<Map> group = groupMapper.getGroup(searchType, word, groupID);

        return group;
    }

    public List getGroupUser(String searchType, String word, List<Map> grpList) throws Exception {
        List<Map> ggu = new ArrayList<>();
        List<Map> allGroup = new ArrayList<>();

        for(Map item: grpList) {
            ggu.addAll(getAllGroup(searchType, word, String.valueOf(item.get("id"))));
        }

        for(Map item: ggu) {
            HashMap groupInfo = new HashMap();
            groupInfo.put("group_id", item.get("id"));
            groupInfo.put("group_name", item.get("group_name"));
            groupInfo.put("group_user", groupMapper.getGroupUser(String.valueOf(item.get("id"))));
            groupInfo.put("group_desc", item.get("group_desc"));

            allGroup.add(groupInfo);
        }
        return allGroup;
    }

    public Object addNewGroup(String groupName, String groupDesc, List<Map> userList) throws Exception {
        Integer result = groupMapper.addNewGroup(groupName, groupDesc);
        for(Map item: userList) {
            groupMapper.updateUserGroup(String.valueOf(item.get("user_id")), result);
        }
        return groupName;
    }

    public Object updateGroup(String groupName, String groupDesc, List<Map> userList, List<Map> blackList) throws Exception {
        Integer result = groupMapper.addNewGroup(groupName, groupDesc);
        for(Map item: userList) {
            groupMapper.updateUserGroup(String.valueOf(item.get("user_id")), result);
        }
        for(Map item: blackList) {
            groupMapper.updateUserToNull(String.valueOf(item.get("user_id")));
        }
        return groupName;
    }

    public Object deleteGroup(List<Map> groupList) throws Exception {
        for (Map item: groupList) {
            List<Map> user = groupMapper.getGroupUser(String.valueOf(item.get("group_id")));
            for(Map us: user) {
                groupMapper.updateUserToNull(String.valueOf(us.get("user_id")));
            }
            groupMapper.deleteGroup(String.valueOf(item.get("group_id")));
        }

        return groupList;
    }
}
