package com.mobigen.datalake;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        // * 외부사이트가 keycloak 연동 후 넘어오는 경우 외부 사이트로부터 Keycloak 인증 후 생성된 Access Token을 받고 처리하는 로직 필요
        // * 외부사이트가 keycloak 연동 후 넘어오지 않는 경우 keycloak 로그인 페이지 또는 포털 로그인 페이지로 리다이렉트
        String redirect_uri = "http://localhost:38080/test";
        String client_id = "data-catalog";
        String realm = "<REALM>";

        String authUrl = "http://<INTERNAL_HOST>:18080/realms/" + realm + "/protocol/openid-connect/auth"
                + "?client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code"
                + "&scope=openid";
        log.info("Redirecting to Keycloak SSO login: {}", authUrl);

        response.sendRedirect(authUrl);
    }

    @GetMapping("/naverLogin")
    public void naverLoginGET(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("GET - naverLoginGET");
        String redirect_uri = "http://localhost:38080/naverTest"; // 네이버 로그인 후 리다이렉트될 URI
        String client_id = "q9HTFVZGTXkf41vmcuV4";

        String authUrl = "https://nid.naver.com/oauth2/authorize"
                + "?response_type=code"
                + "&client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&state=12345" // CSRF 방지를 위한 상태 값
                + "&scope=openid";
        log.info("Redirecting to Naver SSO login: {}", authUrl);

        response.sendRedirect(authUrl);
    }

    @GetMapping("/test")
    public void keycloakTest(HttpServletRequest request, HttpServletResponse response,
    @RequestParam String session_state,
    @RequestParam String iss,
    @RequestParam String code) throws Exception {
        log.info("GET - keycloakTest");
        log.info("session_state: {}", session_state);
        log.info("iss: {}", iss);
        log.info("code: {}", code);

        String redirect_uri = "http://localhost:38080/test";
        String client_id = "data-catalog";
        String realm = "<REALM>";
        // String clientSecret = "mysecret";
        // 
        String tokenUrl = "http://<INTERNAL_HOST>:18080/realms/" + realm + "/protocol/openid-connect/token";

        // 요청 본문 (Form Parameters 형식으로 보낼 데이터)
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code"); // 또는 password 등
        requestBody.add("client_id", client_id);
        // requestBody.add("client_secret", clientSecret); // 클라이언트 인증(public) 필요 시
        requestBody.add("redirect_uri", redirect_uri);
        requestBody.add("code", code); // 인증 코드
        requestBody.add("scope", "openid profile"); // 필요한 스코프 설정, 사용자 정보 조회 시 openid는 필수
        log.info("요청 본문: {}", requestBody);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 엔티티 구성 (헤더 + 바디)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);

        // 결과 출력
        System.out.println("응답 코드: " + res.getStatusCode());
        System.out.println("응답 본문: " + res.getBody());

        response.sendRedirect("/");
    }

    @GetMapping("/naverTest")
    public void naverTest(HttpServletRequest request, HttpServletResponse response,
    @RequestParam(defaultValue = "", required = false) String code,
    @RequestParam(defaultValue = "", required = false) String state,
    @RequestParam(defaultValue = "", required = false) String error,
    @RequestParam(defaultValue = "", required = false) String error_description) throws Exception {
        log.info("GET - naverTest");
        log.info("code: {}", code);
        log.info("state: {}", state);
        log.info("error: {}", error);
        log.info("error_description: {}", error_description);

        String client_id = "<CLIENT_ID>";
        String clientSecret = "mysecret"; // 네이버 개발자 센터에서 발급받은 클라이언트 시크릿

        String tokenUrl = "https://nid.naver.com/oauth2/token"
            + "?grant_type=authorization_code"
            + "&client_id=" + client_id
            + "&client_secret=" + clientSecret
            + "&code=" + code // 인증 코드
            + "&state=" + state; // CSRF 방지를 위한 상태 값

        // 요청 본문 (Form Parameters 형식으로 보낼 데이터)
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        // requestBody.add("grant_type", "authorization_code"); // 또는 password 등
        // requestBody.add("client_id", client_id);
        // requestBody.add("client_secret", clientSecret);
        // requestBody.add("code", code); // 인증 코드
        // requestBody.add("state", state); // CSRF 방지를 위한 상태 값
        // log.info("요청 본문: {}", requestBody);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 엔티티 구성 (헤더 + 바디)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.postForEntity(tokenUrl, requestEntity, String.class);

        // 결과 출력
        System.out.println("응답 코드: " + res.getStatusCode());
        System.out.println("응답 본문: " + res.getBody());

        response.sendRedirect("/");
    }
}
