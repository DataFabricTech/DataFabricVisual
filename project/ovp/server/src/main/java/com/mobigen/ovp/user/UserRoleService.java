package com.mobigen.ovp.user;

import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.entity.UserRoleEntity;
import com.mobigen.ovp.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * cm_user_role에서 "USER" 타입 조회해 OMD_ROLE_ID 를 리스트로 반환
     * @return
     */
    public List<String> getUserRolesIdList() throws Exception {
        UserRoleEntity user = getUserRoleCode();
        if (user == null) {
            throw new Exception("사용자 역할 조회 오류. 관리자에게 문의하세요.");
        }
        List<String> userRoles = new ArrayList<>();
        userRoles.add(user.getOmdRoleId());
        return userRoles;
    }
}
