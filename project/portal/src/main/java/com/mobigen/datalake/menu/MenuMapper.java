package com.mobigen.datalake.menu;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("menuMapper")
public interface MenuMapper {
    List getMenuID(String authority) throws Exception;
    List getMenu(String menuID) throws Exception;
    Map getParentMenu(String menuID) throws Exception;
    Integer clickMenu(String userID, String menuID, String deptID) throws Exception;
    List getMenuHistory() throws Exception;
}
