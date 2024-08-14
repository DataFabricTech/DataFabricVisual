package com.mobigen.ovp.menu.repository;

import com.mobigen.ovp.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String> {
    List<MenuEntity> findAllByUseYnTrueOrderByOrderAsc();
}
