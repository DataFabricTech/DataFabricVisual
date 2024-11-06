package com.mobigen.datalake.atlas;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.util.UriComponentsBuilder;

import com.mobigen.framework.component.Messages;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AtlasRest {
    // @Autowired
    private RestTemplate restTemplate;
    @Value("${properties.atlas.server.url}")
    String baseUrl;

    private RestClient restClient = RestClient.create(baseUrl);

    @Autowired
    protected Messages message;

    @Autowired
    private ATLASProperties prop;


    protected ResponseEntity<Object> methodGet(String url, Object parameter, Map<String, String> extraHeaders,
                                               boolean isResponseEntity) throws Exception {
        return getResult(HttpMethod.GET, url, parameter, extraHeaders, isResponseEntity);
    }

    protected ResponseEntity<Object> methodPost(String url, Object parameter, Map<String, String> extraHeaders,
                                                boolean isResponseEntity) throws Exception {
        return getResult(HttpMethod.POST, url, parameter, extraHeaders, isResponseEntity);
    }

    protected ResponseEntity<Object> methodPut(String url, Object parameter, boolean isResponseEntity)
            throws Exception {
        return getResult(HttpMethod.PUT, url, parameter, null, isResponseEntity);
    }

    protected ResponseEntity<Object> methodDelete(String url, Object parameter, boolean isResponseEntity)
            throws Exception {
        return getResult(HttpMethod.DELETE, url, parameter, null, isResponseEntity);
    }

    private String getUriStringByMap(Map<String, Object> parameter) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        for (Map.Entry<String, Object> entry : parameter.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return builder.toUriString();
    }

    private String getUriStringByObject(Object parameter) throws Exception {// NOPMD
        if(parameter.getClass().getName().equals("java.lang.Boolean")){
           return "";
        }else{
            return URLEncoder.encode((String) parameter, "UTF-8");
        }
    }

    @SuppressWarnings("unchecked")
    private String getUriString(Object parameter) {
        String uriString = "";
        try {
            if (parameter instanceof Map) {
                uriString = getUriStringByMap((Map<String, Object>) parameter);
            } else {
                uriString = getUriStringByObject(parameter);
            }
        } catch (Exception e) {
            log.error(message.get("com.mobigen.framework.utility.restful.RestAPI"), e);
        }

        return uriString;
    }

    private HttpHeaders generateHttpHeaders(Map<String, String> extraHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(prop.getServer().getUser(),prop.getServer().getPasswd());

        if (extraHeaders == null) {
            return headers;
        }

        Iterator<String> keys = extraHeaders.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            headers.add(key, extraHeaders.get(key));
        }

        return headers;
    }

    protected ResponseEntity<Object> getResult(HttpMethod httpMethod, String url, Object parameter,
                                               Map<String, String> extraHeaders, boolean isResponseEntity) throws Exception {
        HttpEntity<Object> request = null;
        ResponseEntity<Object> response = null;
        HttpHeaders headers = generateHttpHeaders(extraHeaders);

        switch (httpMethod.name()) {
            case "GET":
                String uriVariables = getUriString(parameter);
                if (uriVariables != null) {
                    url += uriVariables;
                }
                request = new HttpEntity<Object>(null, headers);
            case "PUT":
            case "DELETE":
                request = new HttpEntity<Object>(parameter, headers);
            case "HEAD":
            case "OPTIONS":
            case "PATCH":
            case "TRACE":
                break;
        }

        // response = restTemplate.exchange(getAtlasPropUrl(url), httpMethod, request, Object.class);        
        
        // response = restClient.method(httpMethod)
        // .uri(getAtlasPropUrl(url))
        // .headers(httpHeaders -> {
        //     httpHeaders.setBasicAuth(prop.getServer().getUser(),prop.getServer().getPasswd());
        // })
        // .body(parameter)
        // .retrieve()
        // .toEntity(Object.class);

        // PUT, DELETE 시에 204 no content로 response가 오면 exception이 발생하여서 예외 처리
        RequestBodySpec tmSpec = restClient.method(httpMethod)
        .uri(getAtlasPropUrl(url))
        .headers(httpHeaders -> {
            httpHeaders.setBasicAuth(prop.getServer().getUser(),prop.getServer().getPasswd());
        });

        ResponseSpec responseSpec;
        if(parameter != null){
            responseSpec = tmSpec.body(parameter).retrieve();
        } else {
            responseSpec = tmSpec.retrieve();
        }

        if(responseSpec != null){
            response = responseSpec.toEntity(Object.class);
        }        
        
        return response;
    }

    // Atlas API 중 Tag Entities 조회 API만 URL 패턴 상이, 별도 메소드 작성
    protected ResponseEntity<Object> getAdminAPIResult(HttpMethod httpMethod, String url, Object parameter,
                                                       Map<String, String> extraHeaders, boolean isResponseEntity) throws Exception {
        HttpEntity<Object> request = null;
        ResponseEntity<Object> response = null;

        HttpHeaders headers = generateHttpHeaders(extraHeaders);
        String uriVariables = getUriString(parameter);
        if (uriVariables != null) {
            url += uriVariables;
        }

        request = new HttpEntity<Object>(null, headers);
        String atlasUrl = UriComponentsBuilder.newInstance()
                .scheme(prop.getServer().getScheme())
                .host(prop.getServer().getHost())
                .port(prop.getServer().getPort())
                .build().toUri().toString()
                .concat("/api/atlas")
                .concat(url);

        // response = restTemplate.exchange(atlasUrl, httpMethod, request, Object.class);
        return response;
    }

    private String getAtlasPropUrl(String url) {
        URI result = UriComponentsBuilder.newInstance()
                .scheme(prop.getServer().getScheme())
                .host(prop.getServer().getHost())
                .port(prop.getServer().getPort())
                .path(prop.getServer().getUrl())
                .build().toUri();
        return result + url;
    }
}
