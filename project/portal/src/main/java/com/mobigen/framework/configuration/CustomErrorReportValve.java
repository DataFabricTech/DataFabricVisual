// package com.mobigen.framework.configuration;

// import jakarta.servlet.ServletException;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.catalina.connector.Request;
// import org.apache.catalina.connector.Response;
// import org.apache.catalina.valves.ValveBase;

// import java.io.IOException;
// import java.util.regex.Pattern;

// @Slf4j
// public class CustomErrorReportValve extends ValveBase {

//     // 경로에서 허용하는 문자만 남기는 정규식 (알파벳, 숫자, 슬래시, 하이픈, 밑줄)
//     private static final Pattern ALLOWED_PATH_PATTERN = Pattern.compile("^[-_/\\.a-zA-Z0-9]+$");

//     @Override
//     public void invoke(Request request, Response response) throws IOException, ServletException {
//         String uri = request.getRequestURI();
//         // log.info("Request URI: " + uri);

//         // 쿼리 파라미터가 포함된 경우 '?'로 경로와 파라미터를 분리
//         String[] pathAndParams = uri.split("\\?", 2);
//         String path = pathAndParams[0]; // 경로 부분
//         String query = pathAndParams.length > 1 ? pathAndParams[1] : ""; // 쿼리 파라미터 부분

//         // 경로에서 특수 문자가 있는지 확인
//         if (!isValidPath(path)) {
//             // response.getWriter().write("{result:\"0\", errorMessage:\"Bad Request\"}");
//             // 특수 문자가 있는 경우 '/'로 리다이렉트
//             response.sendRedirect("/error");
//             log.error("Bad request - invalid characters in path: " + path);
//             return;
//         }

//         // 다음 필터나 서블릿으로 요청 전달
//         getNext().invoke(request, response);
//     }

//     // 경로의 유효성을 검사하는 메서드
//     private boolean isValidPath(String path) {
//         return ALLOWED_PATH_PATTERN.matcher(path).matches();
//     }

// }

