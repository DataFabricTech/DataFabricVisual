package com.mobigen.ovp.email;

import java.util.Optional;
import com.mobigen.framework.utility.Token;
import com.mobigen.ovp.auth.PwResetEntity;
import com.mobigen.ovp.auth.PwResetRepository;
import com.mobigen.ovp.common.OvpProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final Token token;
    private final OvpProperties ovpProperties;
    private final PwResetRepository pwResetRepository;

    /**
     * 비밀번호 재설정 > 링크 유효성 검증 및 Redirect
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    public Object redirectPwRest(HttpServletRequest request, String id) {
        String host = determineHost(request);
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append("redirect:").append(host);

        // 1. 링크 유효성 확인
        Optional<PwResetEntity> pwResetData = pwResetRepository.findById(id);
        if (!pwResetData.isPresent()) {
            redirectUrl.append(ovpProperties.getMail().getRedirectErrorUrl());
            return redirectUrl;
        }
        // 2. 메일 시간 유효성 검증
        if (EmailUtil.isLinkExpiredWithValidTime(pwResetData.get().getValidTime(), pwResetData.get().getCreateDate())) {
            redirectUrl.append(ovpProperties.getMail().getRedirectErrorUrl());
        } else {
            redirectUrl.append(ovpProperties.getMail().getRedirectUrl()).append("?id=").append(id);
        }
        // 3. 리다이랙트
        return redirectUrl;
    }

    /**
     * 비밀번호 > 비밀번호 재설정 링크 생성
     * - NOTE: 로컬 호스트일 경우 Client 서버로 HOST 설정 / 배포 시 화면이랑 같이 묶여 배포되기 때문에 배포시에는 상관없음
     *
     * @param request
     * @return
     */
    private String determineHost(HttpServletRequest request) {
        return Boolean.TRUE.equals(token.isLocal(request))
                ? "http://localhost:3300"
                : request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}
