package com.mobigen.ovp.email;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Controller
public class EmailController {
    private final EmailService emailService;

    /**
     * 비밀번호 재설정 > 링크 유효성 검증 및 Redirect
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/pw-reset/{id}")
    public void redirectPwRest(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("비밀번호 재설정 리다이랙트 {}", id);
        emailService.redirectPwRest(request, response, id);
    }
}
