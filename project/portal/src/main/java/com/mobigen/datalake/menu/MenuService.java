package com.mobigen.datalake.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MenuService {

    private final SessionManager sessionManager;
    private final MenuMapper menuMapper;
    private final UserMapper userMapper;

    private final IRISProperties properties;

    //
    public List<Map> getMenu_back(HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        if (user == null)
            return null;

        String userAuthority = user.get("authority").toString();

        List<Map> menuID = new ArrayList<>();
        List<Map> menu = new ArrayList<>();

        String roleName = user == null || !StringUtils.hasText((String) user.get("authority")) ? "User"
                : (String) user.get("authority");
        if (!userAuthority.equals(roleName)) {// 권한이 변경된 경우 빈 결과값 리턴
            return menu;
        }

        switch (userAuthority) {
            case "Super-Admin":
            case "Admin":
                menuID.addAll(menuMapper.getMenuID("User"));
                menuID.addAll(menuMapper.getMenuID("Admin"));
                break;
            case "Manager":
                menuID.addAll(menuMapper.getMenuID("User"));
                menuID.addAll(menuMapper.getMenuID("Manager"));
                break;
            case "User":
                menuID.addAll(menuMapper.getMenuID("User"));
                break;
        }

        for (Map map : menuID) {
            HashMap allMenu = new HashMap();

            Map parentMenu = menuMapper.getParentMenu((String) map.get("menu_id"));
            List childMenu = menuMapper.getMenu((String) map.get("menu_id"));

            allMenu.put("title", parentMenu.get("menu_name"));
            allMenu.put("authority", map.get("menu_role_name"));
            allMenu.put("menu_visible", parentMenu.get("menu_visible"));
            allMenu.put("child", childMenu);

            menu.add(allMenu);
        }
        return menu;
    }

    //
    public List<Map> getMenuHierarchy(List<Map> menuList) {
        List<Map> roleMenuList = new ArrayList<>();
        Map<String, Map> menuMapById = new HashMap<>();
        Map<String, List<Map>> childrenMapById = new HashMap<>();

        for (Map item : menuList) {
            String menuId = (String) item.get("menu_id");
            menuMapById.put(menuId, item);
        }

        for (Map item : menuList) {
            String parentMenuId = (String) item.get("parent_menu_id");
            if (parentMenuId != null && !parentMenuId.isEmpty()) {
                childrenMapById.computeIfAbsent(parentMenuId, k -> new ArrayList<>()).add(item);
            }
        }

        for (Map item : menuList) {
            String parentMenuId = (String) item.get("parent_menu_id");
            if (parentMenuId == null || parentMenuId.isEmpty()) {
                Map<String, Object> menuMap = createMenuMap(item);
                roleMenuList.add(menuMap);
                addChildren(menuMap, childrenMapById);
            }
        }

        for (Map.Entry<String, List<Map>> entry : childrenMapById.entrySet()) {
            String parentId = entry.getKey();
            if (!menuMapById.containsKey(parentId)) {
                List<Map> orphans = entry.getValue();
                for (Map orphan : orphans) {
                    Map<String, Object> orphanMap = createMenuMap(orphan);
                    roleMenuList.add(orphanMap);
                    addChildren(orphanMap, childrenMapById);
                }
            }
        }

        return roleMenuList;
    }

    //
    private void addChildren(Map<String, Object> parentItem, Map<String, List<Map>> childrenMapById) {
        String parentId = (String) parentItem.get("menu_id");
        List<Map> children = childrenMapById.get(parentId);

        if (children != null) {
            List<Map> childList = new ArrayList<>();
            for (Map child : children) {
                Map<String, Object> childMap = createMenuMap(child);
                childList.add(childMap);
                addChildren(childMap, childrenMapById);
            }
            parentItem.put("child", childList);
        }
    }

    //
    private Map<String, Object> createMenuMap(Map item) {
        String menuId = (String) item.get("menu_id");
        Map<String, Object> menuMap = new HashMap<>();
        menuMap.put("menu_auth", item.get("menu_role_name"));
        menuMap.put("menu_id", item.get("menu_id"));
        menuMap.put("title", item.get("menu_name"));
        menuMap.put("menu_name", item.get("menu_name"));
        menuMap.put("parent_menu_id", item.get("parent_menu_id"));
        menuMap.put("menu_depth", item.get("menu_depth"));
        menuMap.put("menu_visible", item.get("menu_visible"));
        menuMap.put("menu_path", item.get("menu_path"));
        menuMap.put("authority", menuId.startsWith("A") ? "Admin" : "User");
        menuMap.put("child", new ArrayList<Map>());

        return menuMap;
    }

    //
    public List<Map> getMenu(HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        if (user == null)
            return null;

        String userAuthority = user.get("authority").toString();

        List<Map> menuID = new ArrayList<>();
        List<Map> menu = new ArrayList<>();

        String roleName = user == null || !StringUtils.hasText((String) user.get("authority")) ? "User"
                : (String) user.get("authority");
        if (!userAuthority.equals(roleName)) {// 권한이 변경된 경우 빈 결과값 리턴
            return menu;
        }

        List<Map> menuList = menuMapper.getUserMenubyRole((String) user.get("role_id"));

        List<Map> roleMenuList = getMenuHierarchy(menuList);

        // menu_visible이 true인 항목만 필터링
        List<Map> filteredRoleMenuList = roleMenuList.stream()
                .filter(item -> Boolean.TRUE.equals(item.get("menu_visible"))) // menu_visible이 true인 항목만 필터
                .collect(Collectors.toList());

        HashMap menuListMap = new HashMap();
        menuListMap.put("roleMenuList", filteredRoleMenuList);
        menu.add(menuListMap);

        // role menu(검증용)
        Algorithm algorithm = Algorithm.HMAC256(properties.getAccessToken().getSecret());
        HashMap menuRoleListMap = new HashMap();

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("roleMenuList", roleMenuList);
        menuRoleListMap.put("decodeRoleMenuList",
                JWT.create().withPayload(payloadMap).withExpiresAt(new Date()).sign(algorithm));

        menu.add(menuRoleListMap);

        return menu;
    }

    //
    public Object clickMenu(Map<String, Object> params) throws Exception {
        return menuMapper.clickMenu(params);
    }

    //
    public List<Map> getMenuHistory() throws Exception {
        List<Map> result = new ArrayList<>();
        List<Map> history = menuMapper.getMenuHistory();

        result.add(new HashMap() {
            {
                put("id", "User");
                put("name", "User");
                put("color", "#a6cfe2");
            }
        });
        result.add(new HashMap() {
            {
                put("id", "Manager");
                put("name", "Manager");
                put("color", "#e88565");
            }
        });
        result.add(new HashMap() {
            {
                put("id", "Admin");
                put("name", "Admin");
                put("color", "#eed974");
            }
        });

        for (Map item : history) {
            if (item.get("parent").toString().contains("A")) {
                item.put("parent", "Admin");
            } else if (item.get("parent").toString().contains("D")) {
                item.put("parent", "Manager");
            } else {
                item.put("parent", "User");
            }
            result.add(item);
        }

        return result;
    }

    //
    public List<Map> getRoleMenu(String roleType) throws Exception {
        List<Map> menu = new ArrayList<>();

        try {
            String[] roleSplitText = roleType.split(",");

            List<Map> roleMenuList = new ArrayList<>();
            for (String role : roleSplitText) {
                List<Map> menuList = menuMapper.getRoleMenu(role);

                roleMenuList.addAll(getMenuHierarchy(menuList));
            }

            HashMap menuListMap = new HashMap();
            menuListMap.put("roleMenuList", roleMenuList);
            menu.add(menuListMap);

            HashMap roleTypeMap = new HashMap();
            roleTypeMap.put("roleTypeList", menuMapper.getRoleType("0"));
            menu.add(roleTypeMap);

            HashMap roleMenuMap = new HashMap();
            roleMenuMap.put("roleMenuExistsList", menuMapper.getRoleMenuExists());
            menu.add(roleMenuMap);

        } catch (Exception e) {

        }

        return menu;
    }

    //
    public Object roleMenuSave(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

        List<Map<String, Object>> activeList = list.stream()
                .filter(item -> "Y".equals(item.get("use_yn")))
                .collect(Collectors.toList());

        List<Map<String, Object>> inactiveList = list.stream()
                .filter(item -> "N".equals(item.get("use_yn")))
                .collect(Collectors.toList());

        if (inactiveList.size() > 0) {
            menuMapper.menuRoleDelete(inactiveList);
        }

        if (activeList.size() > 0) {
            menuMapper.menuRoleInsert(activeList);
        }

        return result;
    }

}
