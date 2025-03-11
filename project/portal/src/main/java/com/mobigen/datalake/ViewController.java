package com.mobigen.datalake;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ViewController {
//    @GetMapping("/")
//    public String index() {
//        User user = sessionManager.getUser();
//        if (null == user) {
//            return "index";
//        }
//        return "app";
//    }
//
//    @GetMapping("/app")
//    public String app() {
//        return "app";
//    }

    @PostMapping("/ssoLogin")
    public void ssoLoginPOST(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("POST - ssoLoginPOST");
        response.sendRedirect("/");
    }

    @GetMapping("/ssoLogin")
    public void ssoLoginGET(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("GET - ssoLoginGET");
        response.sendRedirect("/");
    }

    public String ssoLoginCheck(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }
}
