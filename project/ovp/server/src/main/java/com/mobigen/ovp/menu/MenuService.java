package com.mobigen.ovp.menu;

import com.mobigen.ovp.menu.entity.MenuEntity;
import com.mobigen.ovp.menu.repository.MenuRepository;
import com.mobigen.ovp.user.UserService;
import com.mobigen.ovp.user.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final UserService userService;

    public List<MenuEntity> getMenus() throws Exception {
        // 메뉴 권한을 체크하여 조회한다.
        UserInfoDTO user = (UserInfoDTO) userService.getUserInfo();
        String menuAuthCode = user.isAdmin() ? "A" : "U";
        
        return menuRepository.findAllByUseYnTrueAndMenuAuthContainingOrderByOrderAsc(menuAuthCode).stream()
                .filter(menu -> !menu.getId().equals(menu.getParentId())) // ROOT 메뉴는 제외한다.
                .collect(Collectors.toList());
    }
}
