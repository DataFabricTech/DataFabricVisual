package com.mobigen.datalake.menu;

import com.mobigen.datalake.user.UserMapper;
import com.mobigen.framework.security.SessionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class MenuService {

    private final SessionManager sessionManager;
    private final MenuMapper menuMapper;
    private final UserMapper userMapper;

    public List<Map> getMenu(HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        if (user == null) return null;

        String userAuthority = user.get("authority").toString();

        List<Map> menuID = new ArrayList<>();
        List<Map> menu = new ArrayList<>();

        String roleName = user == null || !StringUtils.hasText((String)user.get("authority")) ? "User" : (String)user.get("authority");
        if(!userAuthority.equals(roleName)) {// 권한이 변경된 경우 빈 결과값 리턴
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
        
        for (Map map: menuID) {
            HashMap allMenu = new HashMap();

            Map parentMenu = menuMapper.getParentMenu((String) map.get("menu_id"));
            List childMenu = menuMapper.getMenu((String) map.get("menu_id"));

            allMenu.put("title", parentMenu.get("menu_name"));
            allMenu.put("authority", map.get("menu_authority"));
            allMenu.put("menu_visible", parentMenu.get("menu_visible"));
            allMenu.put("child", childMenu);

            menu.add(allMenu);
        }
        return menu;
    }

    public Object clickMenu(String userID, String menuID) throws Exception {
        return menuMapper.clickMenu(userID, menuID);
    }

    public List<Map> getMenuHistory() throws Exception {
        List<Map> result = new ArrayList<>();
        List<Map> history = menuMapper.getMenuHistory();

        result.add(new HashMap(){{put("id", "User"); put("name", "User"); put("color", "#a6cfe2");}});
        result.add(new HashMap(){{put("id", "Manager"); put("name", "Manager"); put("color", "#e88565");}});
        result.add(new HashMap(){{put("id", "Admin"); put("name", "Admin"); put("color", "#eed974");}});

        for(Map item: history) {
            if (item.get("parent").toString().contains("A")) {
                item.put("parent", "Admin");
            } else if (item.get("parent").toString().contains("D")) {
                item.put("parent", "Manager");
            } else {
                item.put("parent", "User");
            }
            result.add(item);
        }


        return  result;
    }
}
