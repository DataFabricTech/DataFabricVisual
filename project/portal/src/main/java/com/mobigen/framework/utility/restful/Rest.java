// package com.mobigen.framework.utility.restful;

// import java.util.Iterator;
// import java.util.Map;

// import com.mobigen.framework.component.Messages;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.util.UriComponentsBuilder;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// public class Rest {
//     @Autowired
//     private RestTemplate restTemplate;

//     @Autowired
//     protected Messages message;

//     protected ResponseEntity<Object> methodGet(String url, Object parameter, Map<String, String> extraHeaders,
//             boolean isResponseEntity) throws Exception {
//         return getResult(HttpMethod.GET, url, parameter, extraHeaders, isResponseEntity);
//     }

//     protected ResponseEntity<Object> methodPost(String url, Object parameter, Map<String, String> extraHeaders,
//             boolean isResponseEntity) throws Exception {
//         return getResult(HttpMethod.POST, url, parameter, extraHeaders, isResponseEntity);
//     }

//     protected ResponseEntity<Object> methodPut(String url, Object parameter, boolean isResponseEntity)
//             throws Exception {
//         return getResult(HttpMethod.PUT, url, parameter, null, isResponseEntity);
//     }

//     protected ResponseEntity<Object> methodDelete(String url, Object parameter, boolean isResponseEntity)
//             throws Exception {
//         return getResult(HttpMethod.DELETE, url, parameter, null, isResponseEntity);
//     }

//     private String getUriStringByMap(Map<String, Object> parameter) throws Exception {
//         UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
//         for (Map.Entry<String, Object> entry : parameter.entrySet()) {
//             builder.queryParam(entry.getKey(), entry.getValue());
//         }
//         return builder.toUriString();
//     }

//     private String getUriStringByObject(Object parameter) throws Exception {// NOPMD
//         return "";
//     }

//     @SuppressWarnings("unchecked")
//     private String getUriString(Object parameter) {
//         String uriString = "";
//         try {
//             if (parameter instanceof Map) {
//                 uriString = getUriStringByMap((Map<String, Object>) parameter);
//             } else {
//                 uriString = getUriStringByObject(parameter);
//             }
//         } catch (Exception e) {
//             log.error(message.get("com.mobigen.framework.utility.restful.RestAPI"), e);
//         }

//         return uriString;
//     }

//     private HttpHeaders generateHttpHeaders(Map<String, String> extraHeaders) {
//         HttpHeaders headers = new HttpHeaders();

//         if (extraHeaders == null) {
//             return headers;
//         }

//         Iterator<String> keys = extraHeaders.keySet().iterator();
//         while (keys.hasNext()) {
//             String key = keys.next();
//             headers.add(key, extraHeaders.get(key));
//         }

//         return headers;
//     }

//     protected ResponseEntity<Object> getResult(HttpMethod httpMethod, String url, Object parameter,
//             Map<String, String> extraHeaders, boolean isResponseEntity) throws Exception {

//         HttpEntity<Object> request = null;
//         ResponseEntity<Object> response = null;

//         HttpHeaders headers = generateHttpHeaders(extraHeaders);

//         switch (httpMethod.name()) {
//             case "GET":
//                 String uriVariables = getUriString(parameter);
//                 if (uriVariables != null) {
//                     url += uriVariables;
//                 }
//                 request = new HttpEntity<Object>(null, headers);
//                 break;
//             case "POST":
//             case "PUT":
//             case "DELETE":
//                 request = new HttpEntity<Object>(parameter, headers);
//                 break;
//             case "HEAD":
//             case "OPTIONS":
//             case "PATCH":
//             case "TRACE":
//                 break;
//         }

//         response = restTemplate.exchange(url, httpMethod, request, Object.class);
//         return response;
//     }
// }
