package com.mobigen.datalake.atlas;

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.mobigen.framework.result.JsonResult;

@Component
public class AtlasRestAPI extends AtlasRest {
    private JsonResult getJsonResult(ResponseEntity<Object> response, boolean isResponseEntity) {
        JsonResult result = null;
        try {

            if (!isResponseEntity) {
                result = new JsonResult(response.getBody());
            } else {
                result = new JsonResult(response);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return result;
    }

    private JsonResult getErrorJsonResult(Exception e) {
        JsonResult result = new JsonResult();
        result.setErrorMessage(e);
        return result;
    }

    public JsonResult get(String url) {
        return this.get(url, false);
    }

    public JsonResult get(String url, Object parameter) {
        return this.get(url, parameter, null, false);
    }

    public JsonResult get(String url, Object parameter, Map<String, String> extraHeaders) {
        return this.get(url, parameter, extraHeaders, false);
    }

    public JsonResult get(String url, Object parameter, Map<String, String> extraHeaders, boolean isResponseEntity) {
        ResponseEntity<Object> r = null;
        try {
            r = super.methodGet(url, parameter, extraHeaders, isResponseEntity);
        } catch (Exception e) {
            return getErrorJsonResult(e);
        }

        return getJsonResult(r, isResponseEntity);
    }

    public JsonResult post(String url, Object parameter) {
        return post(url, parameter, false);
    }

    public JsonResult post(String url, Object parameter, boolean isResponseEntity) {
        ResponseEntity<Object> r = null;
        try {
            r = super.methodPost(url, parameter, null, isResponseEntity);
        } catch (Exception e) {
            return getErrorJsonResult(e);
        }

        return getJsonResult(r, isResponseEntity);
    }

    public JsonResult post(String url, Object parameter, Map<String, String> extraHeaders, boolean isResponseEntity) {
        ResponseEntity<Object> r = null;
        try {
            r = super.methodPost(url, parameter, extraHeaders, isResponseEntity);
        } catch (Exception e) {
            return getErrorJsonResult(e);
        }

        return getJsonResult(r, isResponseEntity);
    }

    public JsonResult put(String url, Object parameter, boolean isResponseEntity) {
        ResponseEntity<Object> r = null;
        try {
            r = super.methodPut(url, parameter, isResponseEntity);
        } catch (Exception e) {
            return getErrorJsonResult(e);
        }

        return getJsonResult(r, isResponseEntity);
    }

    public JsonResult delete(String url, Object parameter, boolean isResponseEntity) {
        ResponseEntity<Object> r = null;
        try {
            r = super.methodDelete(url, parameter, isResponseEntity);
        } catch (Exception e) {
            return getErrorJsonResult(e);
        }

        return getJsonResult(r, isResponseEntity);
    }

    public JsonResult getAdminAPI(String url) {
        ResponseEntity<Object> r = null;
        try {
            r = super.getAdminAPIResult(HttpMethod.GET, url, null, null, false);
        } catch (Exception e) {
            return getErrorJsonResult(e);
        }

        return getJsonResult(r, false);
    }
}