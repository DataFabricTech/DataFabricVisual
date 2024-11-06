package com.mobigen.datalake.system.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mobigen.framework.security.SessionManager;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final SessionManager sessionManager;

    @Value("${properties.loginDeactivationDateThreshold}")
    int loginDeactivationDateThreshold;

    /**
     * 사용자 목록 조회
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map> getUserList(Map<String, Object> param) throws Exception {
        param.put("loginDeactivationDateThreshold", loginDeactivationDateThreshold);
        List<Map> user = (List<Map>) adminMapper.getUserList(param);
        return user;
    }

    /**
     * 사용자 목록 개수 조회
     * @param param
     * @return
     * @throws Exception
     */
    public Integer getUserListTotal(Map<String, Object> param) throws Exception {
        Integer total = adminMapper.getUserListTotal(param);
        return total;
    }

    /**
     * 사용자 목록 조회 (권한 검색 시)
     * @param param
     * @return
     * @throws Exception
     */
    public List<Map> getUserListWithRoleName(Map<String, Object> param) throws Exception {
        param.put("loginDeactivationDateThreshold", loginDeactivationDateThreshold);
        List<Map> user = (List<Map>) adminMapper.getUserListWithRoleName(param);
        return user;
    }

    /**
     * 사용자 목록 개수 조회 (권한 검색 시)
     * @param param
     * @return
     * @throws Exception
     */
    public Integer getUserListTotalWithRoleName(Map<String, Object> param) throws Exception {
        Integer total = adminMapper.getUserListTotalWithRoleName(param);
        return total;
    }

    /**
     * 사용자 단건 조회
     * @param user_id
     * @return
     * @throws Exception
     */
    public Object getUser(String user_id) throws Exception {
        Map<String, Object> user = (Map<String, Object>) adminMapper.getUser(user_id);
        return user;
    }

    /**
     * 사용자 정보 수정 (권한)
     * @param param
     * @return
     * @throws Exception
     */
    public Object updateUserInfo(Map<String, String> param) throws Exception {
        Object result = adminMapper.updateUserInfo(param);
        return result;
    }

    /**
     * 장기 미접속 해제
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    public Object unlockLoginDeactivation(Map<String, Object> param, HttpServletRequest request) throws Exception {
        Map<String, Object> user = sessionManager.getUserByAccessToken(request);
        Object result = null;

        if (user.get("authority").toString().equals("Super-Admin") || user.get("authority").toString().equals("Manager")) {
            result = adminMapper.UnlockLoginDeactivation(param);
        }
        
        return result;
    }

    

}
