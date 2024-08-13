package com.mobigen.ovp.menu;

import com.mobigen.ovp.menu.entity.MenuEntity;
import com.mobigen.ovp.menu.repository.MenuRepository;
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

    public List<MenuEntity> getMenus() {
        // ROOT 메뉴는 제외한다.
        return menuRepository.findAllByUseYnTrueOrderByOrderAsc().stream()
                .filter(menu -> !menu.getId().equals(menu.getParentId()))
                .collect(Collectors.toList());
    }
}
