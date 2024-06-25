package com.mobigen.framework.utility.restful;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HttpInterfaceInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        HttpHeaders headers = request.getHeaders();
        log.info("Interceptor 호출 " + request.getURI());

        // TODO : Access Token Header에 동적 설정 필요 -> Token 정보를 어디에서 가져올지 확인 필요
        // TODO : Security 스레드 공유 메모리 확인 필요
        return execution.execute(request, body);
    }
}
