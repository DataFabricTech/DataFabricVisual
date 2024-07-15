package com.mobigen.ovp.user;

import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.entity.UserRoleEntity;
import com.mobigen.ovp.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    /**
     * cm_user_role에서 "USER" 타입 조회
     * @return
     */
    public UserRoleEntity getUserRoleCode() {
        return userRoleRepository.findByRole(UserRole.USER).orElse(null);
    }
}
