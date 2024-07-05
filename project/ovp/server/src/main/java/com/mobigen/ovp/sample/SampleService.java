package com.mobigen.ovp.sample;

import com.mobigen.framework.utility.Token;
import com.mobigen.ovp.sample.client.OpenMetadataClient;
import com.mobigen.ovp.sample.client.SampleClient;
import com.mobigen.ovp.sample.client.TestApiClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;


@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {
    private final TestApiClient testApiClient;
    private final SampleClient sampleClient;
    private final OpenMetadataClient openMetadataClient;
    private final Token token;

    public Object getDynamicAPI() {
        UriBuilderFactory uriBuilderfactory = new DefaultUriBuilderFactory("https://httpbin.org/get");
        return testApiClient.getDynamicAPI(uriBuilderfactory);
    }

    public Object getSampleApi() {
        return sampleClient.getSampleApi();
    }

    public Object getErrorApi() {
        return sampleClient.getErrorApi();
    }

    public Object getAnnotationErrorMessage() throws Exception {
        throw new Exception();
    }
    public Object getBasicErrorMessage() throws Exception {
        throw new Exception("기본 에러 메세지 출력");
    }
    public Object getMultiErrorMessage() throws Exception {
        throw new Exception("중복 - 기본 에러 메세지 출력");
    }

    public Object getUsers(HttpServletRequest request) throws Exception {
        log.info("open metadata sample - token: {}", token.getTokensByRequest(request));
        return openMetadataClient.getUsers();
    }

    public Object getSearchList(MultiValueMap<String, String> params) throws Exception {
        return openMetadataClient.getSearchList(params);
    }

    public Object getTableDetail(String table, String fields, String include) throws Exception {
        return openMetadataClient.getTableDetail(table, fields, include);
    }
}
