package com.mobigen.ovp.mypage;

import com.mobigen.ovp.mypage.client.MyPageClient;
import com.mobigen.ovp.mypage.dto.response.UserInfoResponse;
import com.mobigen.ovp.user.UserService;
import com.mobigen.ovp.user.entity.UserEntity;
import com.mobigen.ovp.user.entity.UserRole;
import com.mobigen.ovp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageClient myPageClient;
    private final UserService userService;

    /**
     * 사용자 정보 조회
     *
     * @return
     * @throws Exception
     */
    public Object getUserInfo(String fqn) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fields", "profile,roles,teams,personas,defaultPersona,domain");
        Map<String, Object> openMetaUserInfo = myPageClient.getUserInfo(fqn, params);

        Map<String, Object> newUserInfo = new HashMap<>();
        String id = (String) openMetaUserInfo.get("id");

        newUserInfo.put("id", id);
        newUserInfo.put("description", openMetaUserInfo.get("description"));
        newUserInfo.put("displayName", openMetaUserInfo.get("displayName"));
        newUserInfo.put("name", openMetaUserInfo.get("name"));
        newUserInfo.put("email", openMetaUserInfo.get("email"));
        newUserInfo.put("isAdmin", openMetaUserInfo.get("isAdmin"));

        Optional<UserEntity> dbUserinfo = (Optional<UserEntity>) userService.getUserInfo(id);
        UserEntity userEntity = dbUserinfo.get();
        newUserInfo.put("role", userEntity.getUserRole().toString());

        return new UserInfoResponse(newUserInfo);
    }

    /**
     * 마이페이지 - 사용자 정보(표시이름, 설명, 역할) 수정
     *
     * @param id
     * @param params
     * @return
     * @throws Exception
     */
    public Object updateUserInfo(String id, Map<String, Object> params) throws Exception {

        Optional<UserEntity> loginUserInfo = (Optional<UserEntity>) userService.getUserInfo((String) params.get("loginId"));
        UserEntity loginUserEntity = loginUserInfo.get();

        Optional<UserEntity> editUserInfo = (Optional<UserEntity>) userService.getUserInfo(id);
        UserEntity editUserEntity = editUserInfo.get();

        UserRole loginUserRole = loginUserEntity.getUserRole();
        String loginUserName = loginUserEntity.getName().toString();

        String editUserName = editUserEntity.getName().toString();

        // NOTE: 로그인한 계정이 자신의 계정 수정
        if (loginUserInfo.equals(editUserInfo)) {

            if (loginUserRole.equals(UserRole.USER) && params.get("path").equals("/isAdmin")) {
                throw new Exception("사용자 정보 변경 실패. 관리자에게 문의하세요.");
            }

            if (editUserName.equals("admin")) {
                if (params.get("path").equals("/isAdmin")) {
                    throw new Exception("사용자 정보 변경 실패. 관리자에게 문의하세요.");
                }
            }

        } else {
            // NOTE: 로그인한 계정이 다른 계정 수정

            if (loginUserRole.equals(UserRole.USER)) {
                throw new Exception("사용자 정보 변경 실패. 관리자에게 문의하세요.");
            }

            if (loginUserRole.equals(UserRole.ADMIN) && !loginUserName.equals("admin") && editUserName.equals("admin")) {
                throw new Exception("사용자 정보 변경 실패. 관리자에게 문의하세요.");
            }
        }

        params.remove("loginId");

        List<Map<String, Object>> mapParams = new ArrayList<>();
        mapParams.add(params);

        if (params.get("path").equals("/isAdmin")) {
            Boolean isAdmin = (Boolean) params.get("value");
            userService.updateUserInfo(id, isAdmin);
        }
        myPageClient.UpdateUserInfo(id, mapParams);

        return null;
    }

}
