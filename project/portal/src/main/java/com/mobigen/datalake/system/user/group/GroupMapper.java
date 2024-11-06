package com.mobigen.datalake.system.user.group;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("groupMapper")
public interface GroupMapper {
    List getGroup(String searchType, String word, String groupID) throws Exception;
    List getGroupUser(String groupID) throws Exception;
    Integer addNewGroup(String groupName, String groupDesc) throws Exception;
    Integer updateUserGroup(String userID, Integer groupID) throws Exception;
    Integer updateUserToNull(String userID) throws Exception;
    Integer deleteGroup(String groupID) throws Exception;
}
