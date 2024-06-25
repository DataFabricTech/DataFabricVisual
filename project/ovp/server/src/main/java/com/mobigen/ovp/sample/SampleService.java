package com.mobigen.ovp.sample;

import com.mobigen.ovp.sample.client.SampleClient;
import com.mobigen.ovp.sample.client.TestApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;


@Service
@RequiredArgsConstructor
public class SampleService {
    private final TestApiClient testApiClient;
    private final SampleClient sampleClient;

    public Object getDynamicAPI() {
        UriBuilderFactory uriBuilderfactory = new DefaultUriBuilderFactory("https://httpbin.org/get");
        return testApiClient.getDynamicAPI(uriBuilderfactory);
    }

    public Object getUsers() {
        return sampleClient.getUsers();
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

}
