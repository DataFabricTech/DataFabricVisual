package com.mobigen.datalake;

import com.mobigen.datalake.user.UserMapper;
import com.mobigen.datalake.user.UserService;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.IpAddressUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ViewController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Value("${sso.hkmcenc-id}")
    String HKMCENC_ID;

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

    @PostMapping("/ssoLogin") // Autoway와 연동 POST 타고옴
    public void ssoLoginPOST(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("POST - ssoLoginPOST");
        // SSO 로그인 체크 및 User_ID 유효성 검증
        String User_ID = ssoLoginCheck(request, response); // 예: H110000704828
        // if (User_ID == null || !User_ID.startsWith("H110000") || User_ID.length() != 15) {
        //     throw new IllegalArgumentException("Invalid User ID format");
        // }

        // 사번 추출 및 유효성 검사
        String user_id = User_ID.replace("H110000", ""); // 사번 추출
        // if (user_id == null || user_id.trim().isEmpty() || !user_id.matches("\\d+")) {
        //     throw new IllegalArgumentException("Invalid user ID");
        // }

        String token = userService.generateAccessToken(user_id);
        // 토큰 발급
        Cookie accessToken = new Cookie("x-access-token", token);
        Cookie refreshToken = new Cookie("x-refresh-token", userService.generateRefreshToken(user_id));
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        // 사용자 정보 조회 및 검증
        Map user = (Map) userMapper.getUserMaster(user_id);
        if (user == null || !user.containsKey("user_nm")) {
            throw new IllegalArgumentException("User not found");
        }

        // HashMap userInfo = new HashMap<String, String>();
        // userInfo.put("user_id", user_id);
        // userInfo.put("username", user.get("user_nm").toString());
        // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
        
        // 로그인 이력 추가
        try {
            userService.insertLoginHistory(request, token);
        } catch (Exception e) {
            log.error("Failed to insert login history", e);
            throw new RuntimeException("Failed to log login history", e);
        }
        
        response.sendRedirect("/");
    }

    @GetMapping("/ssoLogin") // Autoway와 연동
    public void ssoLoginGET(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("GET - ssoLoginGET");
        // 사용자 인증 확인 및 유효성 검사
        String User_ID = ssoLoginCheck(request, response);  // H110000704828 : H110000 + 사번
        // if (User_ID == null || !User_ID.startsWith("H110000")) {
        //     throw new IllegalArgumentException("Invalid User ID format");
        // }
        
        // 사번 추출 및 유효성 검사
        String user_id = User_ID.replace("H110000", "");
        // if (user_id == null || user_id.trim().isEmpty()) {
        //     throw new IllegalArgumentException("Invalid user ID");
        // }

        String token = userService.generateAccessToken(user_id);
        // 토큰 발급
        Cookie accessToken = new Cookie("x-access-token", token);
        Cookie refreshToken = new Cookie("x-refresh-token", userService.generateRefreshToken(user_id));
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        // 사용자 정보 조회 및 검증
        Map user = (Map) userMapper.getUserMaster(user_id);
        if (user == null || !user.containsKey("user_nm")) {
            throw new IllegalArgumentException("User not found or invalid user data");
        }

        // HashMap userInfo = new HashMap<String, String>();
        // userInfo.put("user_id", user_id);
        // userInfo.put("username", user.get("user_nm").toString());
        // userInfo.put("user_ip_addr", IpAddressUtil.getClientIp(request));
        try {
            userService.insertLoginHistory(request, token);    // 안전하게 데이터베이스에 로그인 이력 기록
        } catch (Exception e) {
            log.error("Failed to insert login history", e);
            throw new RuntimeException("Failed to log login history", e);
        }
        
        response.sendRedirect("/");
    }

    public String ssoLoginCheck(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> userInfo = new HashMap<>();

        String strHKMCENC_ID = HKMCENC_ID; // 운영은 "BDPDCPROD"
        String strEncCode = ""; // 그룹웨어에서 넘어온 값, 5W9Oqkl23weiofhqowioiqr...e==, // Dept_Code, Dept_name, Pos_Code, Pos_name, User_ID, User_Name, IP, ssoTIME
        String strCompanyCode = ""; // 그룹웨어에서 넘어온 값, H110
        String val1 = ""; // 강제 로그인 사번
        if (request.getParameter("Encode") != null) { strEncCode = request.getParameter("Encode"); }
        if (request.getParameter("CompanyCode") != null) { strCompanyCode = request.getParameter("CompanyCode"); }
        if (request.getParameter("val1") != null) { val1 = request.getParameter("val1"); }

        if (val1 != null && !"".equals(val1)) { // 사번 파라미터가 있으면
            userInfo.put("User_ID", val1);
            return userInfo.get("User_ID").toString(); // val1로 들어온 파라미터(사번)을 바로 리턴 처리 (SSO 인증 미실행)
        }
        javax.net.ssl.HttpsURLConnection httpsConn = null;
        try {
            InetAddress local = InetAddress.getLocalHost();
            String ip = local.getHostAddress();

            String strWSURL = "https://autoway.hyundai.net/WebServices/Common/SitemapWSF/SitemapWS.asmx";

            URL url = new URI(strWSURL).toURL();
            httpsConn = (javax.net.ssl.HttpsURLConnection) url.openConnection();

            httpsConn.setRequestMethod("POST");
            // httpConn.setRequestProperty("User-Agent", USER_AGENT);
            httpsConn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            httpsConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

            StringBuffer sbSoapParam = new StringBuffer();

            sbSoapParam.append("<?xml version='1.0' encoding='utf-8'?>");
            sbSoapParam.append("<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>");
            sbSoapParam.append("<soap:Body>");
            sbSoapParam.append("<GetPlainText xmlns='http://tempuri.org/'>");
            sbSoapParam.append("<strEncID>").append(strHKMCENC_ID).append("</strEncID>");
            sbSoapParam.append("<strCompanyCode>").append(strCompanyCode).append("</strCompanyCode>");
            sbSoapParam.append("<strEncText>").append(strEncCode).append("</strEncText>");
            sbSoapParam.append("</GetPlainText>");
            sbSoapParam.append("</soap:Body>");
            sbSoapParam.append("</soap:Envelope>");

            log.info("sbSoapParam : " + sbSoapParam.toString());

            httpsConn.setDoOutput(true);
            try (DataOutputStream dos = new DataOutputStream(httpsConn.getOutputStream())) {
                dos.writeBytes(sbSoapParam.toString());
                dos.flush();
            } catch (Exception e) {
                log.error("Error while writing to output stream: ", e);
            }

//            int iResCode = httpsConn.getResponseCode();

            StringBuffer sbResp = new StringBuffer();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()))) {
                String sTmpLine;
                while ((sTmpLine = br.readLine()) != null) {
                    sbResp.append(sTmpLine);
                }
            } catch (Exception e) {
                log.error("Error while reading from HTTP connection: ", e);
                throw new RuntimeException("Failed to read response from HTTP connection", e);
            }

            int iPos1 = sbResp.indexOf("<GetPlainTextResult>") + "<GetPlainTextResult>".length();
            int iPos2 = sbResp.indexOf("</GetPlainTextResult>");
            String sRstVal = sbResp.substring(iPos1, iPos2); // Dept_Code||H1101ERP02___Dept_Name||데이터사이언스팀___......
            

            // sRstVal 값을 ___로 분리
            String[] sRstValList = sRstVal.split("___");
            
            for (int i=0; i < sRstValList.length; i++) {
                String[] keyValue = sRstValList[i].split("\\|\\|");
                userInfo.put(keyValue[0], keyValue[1]);
            }

            return userInfo.get("User_ID").toString();

        } catch (Exception e) {
            e.printStackTrace();;
        } finally {
            if (httpsConn != null) {
                httpsConn.disconnect(); // HttpsURLConnection 해제
            }
        }
        return "";
    }
}
