package com.mobigen.ovp.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cm_user_role")
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity {
    @Id
    @Column(name = "id", nullable = false, length = 55)
    private String id;

    @NotNull
    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "omd_role_id", length = 55)
    private String omdRoleId;

}
