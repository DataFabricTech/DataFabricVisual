package com.mobigen.framework.utility;

import jakarta.servlet.http.HttpServletRequest;

public class IpAddressUtil {
  
  public static String getClientIp(HttpServletRequest request) {

    String ip = request.getHeader("X-Forwarded-For");    
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      // X-Forwarded-For 헤더에 여러 개의 IP가 있을 수 있으므로, 첫 번째 IP를 가져옵니다.
      if (ip.contains(",")) {
          ip = ip.split(",")[0];
      }
    } else {
      ip = request.getHeader("X-Real-IP");
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
          ip = request.getRemoteAddr();  // 프록시가 없는 경우, 기본 IP 주소 가져오기
      }
    }
    return ip;
  }
}
