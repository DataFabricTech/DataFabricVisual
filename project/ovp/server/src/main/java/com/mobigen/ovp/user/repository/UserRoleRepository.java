package com.mobigen.ovp.user.repository;

import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    Optional<UserRoleEntity> findByRole(UserRole role);
}
