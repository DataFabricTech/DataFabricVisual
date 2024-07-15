package com.mobigen.ovp.user;

import com.mobigen.ovp.auth.AuthService;
import com.mobigen.ovp.user.entity.UserEntity;
import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    /**
     * 사용자 > 모든 사용자 리스트 조회
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAllUserList() throws Exception {
        // 0. 관리자 TOKEN 발급
        HttpHeaders headers = authService.adminLoginHeader();

        Map<String, Object> param = new HashMap<>();
        param.put("limit", 1000000); // 모든 유저 목록을 조회해 데이터 삽입
        param.put("isBot", false);
        Map<String, Object> result = userClient.getUsers(headers, param);

        return (List<Map<String, Object>>) result.get("data");
    }

    /**
     * 사용자 > Open metadata의 User와 동기화 하는 코드
     *
     * @return
     */
    @Transactional
    public Object synchronizeUser() throws Exception {
        // 1. 모든 UserList 조회
        List<Map<String, Object>> userList = getAllUserList();

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
