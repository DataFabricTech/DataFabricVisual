package com.mobigen.ovp.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_web_menu")
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "order", nullable = false)
    private int order;

    @Column(name = "link_to", nullable = false)
    private String linkTo;

    @Column(name = "icon_name")
    private String iconName;

    @Column(name = "show_yn", nullable = false)
    private boolean showYn;

    @Column(name = "use_yn", nullable = false)
    private boolean useYn;

    @Column(name = "menu_auth", nullable = false)
    private String menuAuth;
}
