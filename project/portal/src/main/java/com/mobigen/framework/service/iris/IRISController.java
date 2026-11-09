package com.mobigen.framework.service.iris;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Brick에서 서비스에 전달하는 이벤트 처리를 위한 Controller 
// - /status: 서비스 상태를 Brick에 반환 
// - /route: 메뉴 서비스 등의 연결 정보를 Brick에 반환 
// - /event: Brick에서 각 서비스에 전달하는 내용을 수신
@RestController
@RequestMapping("/api")
public class IRISController {
    @Autowired
    private IRISService irisService;

    // 서비스 상태 반환
    @GetMapping(value = "/status")
    public Object getStatus() throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", true);
        return resultMap;
    }

    // 서비스 정보 반환
    @GetMapping(value = "/route")
    public Object getRoute(@RequestParam(value = "locale", defaultValue = "") String locale) throws Exception {
        return irisService.getRoute(locale);
    }

    // Brick 이벤트 수신
    @PostMapping(value = "/event")
    public Object getEvent(HttpServletRequest request, @RequestBody Map<String, Object> body) throws Exception {
        return null;
    }

    // brick/angora 생명 연장
    // @GetMapping(value = "/heartbeat")
    // public void heartbeat(HttpServletRequest request) throws Exception {
    //     irisService.heartbeat(request);
    // }
}
