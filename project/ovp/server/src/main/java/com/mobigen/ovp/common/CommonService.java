package com.mobigen.ovp.common;

import com.mobigen.ovp.user.UserRoleService;
import com.mobigen.ovp.user.UserService;
import com.mobigen.ovp.user.entity.UserEntity;
import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    /**
     * 사용자 > Open metadata의 User와 동기화 하는 코드
     *
     * @return
     */
    @Transactional
    public Object synchronizeUser() throws Exception {
        // 1. 모든 UserList 조회
        List<Map<String, Object>> userList = userService.getAllUserList();

        // 2. 현재 저장되어있는 데이터 모두 삭제
        userRepository.deleteAll();

        // 4. 데이터 모두 저장 saveAll
        for (Map<String, Object> user : userList) {
            Boolean isAdmin = (Boolean) user.get("isAdmin");
            List<String> userRoles = userRoleService.getUserRolesIdList();

            UserRole roleCode = UserRole.USER;
            if (Boolean.TRUE.equals(isAdmin)) {
                roleCode = UserRole.ADMIN;
            }

            if (user.get("roles") == null || !((List<String>) user.get("roles")).contains(userRoles.get(0))) {
                log.info("User ROLE 정보 없음. email : {}", user.get("email"));
            }
            userRepository.save(new UserEntity(
                    (String) user.get("id"),
                    (String) user.get("name"),
                    (String) user.get("email"),
                    roleCode
            ));
        }

        return "동기화 완료";
    }
}
