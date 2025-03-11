package com.mobigen.datalake.menu;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("menuMapper")
public interface MenuMapper {
    List getMenuID(String authority) throws Exception;

    List getMenu(String menuID) throws Exception;

    Map getParentMenu(String menuID) throws Exception;

    List getUserMenubyRole(String roleId) throws Exception;

    Integer clickMenu(String userID, String menuID, String deptID) throws Exception;

    List getMenuHistory() throws Exception;

    List getRoleMenu(String roleType) throws Exception;

    List getRoleType(String deptID) throws Exception;

    List getRoleMenuExists() throws Exception;

    Integer menuRoleDelete(List<Map<String, Object>> list) throws Exception;

    Integer menuRoleInsert(List<Map<String, Object>> list) throws Exception;
}
