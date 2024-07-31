package com.mobigen.ovp.sample;

import com.mobigen.ovp.sample.client.OpenMetadataClient;
import com.mobigen.ovp.sample.client.SampleClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleClient sampleClient;
    private final OpenMetadataClient openMetadataClient;

    /**
     * Sample : OpenFeign 사용 예시
     * @return
     */
    public Object getSampleApi() {
        return sampleClient.getSampleApi();
    }

    /**
     * Sample : OpenFeign 사용 예시 - API 오류
     * @return
     */
    public Object getErrorApi() {
        return sampleClient.getErrorApi();
    }

    /**
     * Sample : OpenFeign 사용 예시 - 동적 URL 사용
     * - Client에 BaseURL이 설정되어 있어도 URI 넘길시 해당 설정 BaseUrl을 무시하고 URI 값을 따라감
     *
     * @return
     */
    public Object getDynamicAPI() throws Exception{
        URI uri = new URI("https://httpbin.org/get");
        return sampleClient.getDynamicAPI(uri);
    }

    /**
     * Sample : 어노테이션 에러 출력
     * @return
     * @throws Exception
     */
    public Object getAnnotationErrorMessage() throws Exception {
        throw new Exception();
    }

    /**
     * Sample : 에러 처리
     * @return
     * @throws Exception
     */
    public Object getBasicErrorMessage() throws Exception {
        throw new Exception("기본 에러 메세지 출력");
    }

    /**
     * Sample : 어노테이션 에러 사용 시 우선 출력
     * @return
     * @throws Exception
     */
    public Object getMultiErrorMessage() throws Exception {
        throw new Exception("중복 - 기본 에러 메세지 출력");
    }

    public Object usePatchApi(@RequestBody Map<String, Object> param) {
        Object res = sampleClient.usePatchApi(param);
        return res;
    }

    /**
     * Open Metadata - 사용자 목록 조회
     * @return
     * @throws Exception
     */
    public Object getUsers() throws Exception {
        return openMetadataClient.getUsers();
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     * @param params
     * @return
     * @throws Exception
     */
    public Object getSearchList(MultiValueMap<String, String> params) throws Exception {
        return openMetadataClient.getSearchList(params);
    }

    /**
     * Open Metadata - 탐색 목록 조회(RequestParam 사용)
     * @param table
     * @param fields
     * @param include
     * @return
     * @throws Exception
     */
    public Object getTableDetail(String table, String fields, String include) throws Exception {
        return openMetadataClient.getTableDetail(table, fields, include);
    }

}
