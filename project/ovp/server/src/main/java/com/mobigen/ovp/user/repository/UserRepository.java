package com.mobigen.ovp.user.repository;

import com.mobigen.ovp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByEmail(String email);
}
