package com.mobigen.framework.utility.restful;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Interface를 ProxyFactory에 등록
 */
@Slf4j
public class SimpleHttpInterfaceFactory {
    public <S> S create(Class<S> clientClass) {
        // Custom 어노테이션 등록 확인
        AutoRegisterApi autoRegisterApi = AnnotationUtils.getAnnotation(clientClass, AutoRegisterApi.class);
        if (autoRegisterApi == null) {
            throw new IllegalStateException("AutoRegisterApi annotation not found");
        }

        // BaseUrl 확인
        HttpExchange httpExchange = AnnotationUtils.getAnnotation(clientClass, HttpExchange.class);
        String baseUrl = httpExchange == null ? "" : httpExchange.url();
        if (!StringUtils.hasText(baseUrl)) {
            log.info("BaseUrl 없음");
        }

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(createClient(baseUrl)))
                .build()
                .createClient(clientClass);
    }
    public RestClient createClient(String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(new HttpInterfaceInterceptor())
                .build();
    }
}
