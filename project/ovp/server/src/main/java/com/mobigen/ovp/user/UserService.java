package com.mobigen.ovp.user;

import com.mobigen.ovp.auth.AuthService;
import com.mobigen.ovp.user.entity.UserEntity;
import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    private final AuthService authService;
    private final UserRepository userRepository;

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
     * 사용자 > 토큰을 통한 사용자 세부 정보 출력
     *
     * @return
     * @throws Exception
     */
    public Object getUserInfo() throws Exception {
        return userClient.getUserInfo();
    }

    /**
     * 사용자 > id로  사용자 정보 DB 조회
     *
     * @return
     * @throws Exception
     */
    public Object getUserInfo(String userId) throws Exception {
        return userRepository.findById(userId);
    }

    /**
     * 사용자 > id로  사용자 정보 DB 조회 후 역할 수정
     *
     * @return
     * @throws Exception
     */
    public Object updateUserInfo(String userId, Boolean isAdmin) throws Exception {
        Optional<UserEntity> userInfo = userRepository.findById(userId);
        UserRole role = isAdmin ? UserRole.ADMIN : UserRole.USER;
        if (userInfo.isPresent()) {
            UserEntity user = userInfo.get();
            user.setUserRole(role);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + userId);
        }
    }
}
