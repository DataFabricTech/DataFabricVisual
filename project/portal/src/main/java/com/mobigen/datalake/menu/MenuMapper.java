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
    List getRollMenu(String rollType) throws Exception;
    List getRollType(String deptID) throws Exception;
    List getRollMenuExists() throws Exception;
    Integer menuRollDelete(List<Map<String, Object>> list) throws Exception;
    Integer menuRollInsert(List<Map<String, Object>> list) throws Exception;
}
