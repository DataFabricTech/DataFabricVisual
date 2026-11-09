package com.mobigen.datalake;

import com.mobigen.framework.security.SessionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@AllArgsConstructor
public class ViewErrorController implements ErrorController {
    private final SessionManager sessionManager;
//    /**
//     * Vue Route가 History 모드 일 경우, route 설정대로 Controller 에
//     * 넘어오게 되어 있는데 이때 세션이 있는 상태이면 메인 페이지로 돌릴 수 있어야 함
//     */
//    @GetMapping("/error")
//    public String error(HttpServletRequest request) {
//        log.info(request.getRequestURI());
//        return "redirect:" + request.getRequestURI() + "/index.html";
//    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/400";  // 400 에러일 때 400.html을 반환
            }
        }
        return "error";  // 다른 에러들은 기본 에러 페이지로 처리
    }

}
