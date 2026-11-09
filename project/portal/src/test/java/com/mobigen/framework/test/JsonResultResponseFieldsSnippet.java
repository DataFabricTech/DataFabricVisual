package com.mobigen.framework.test;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class JsonResultResponseFieldsSnippet extends AbstractFieldsSnippet {

    protected JsonResultResponseFieldsSnippet(List<FieldDescriptor> descriptors, Map<String, Object> attributes,
            boolean ignoreUndocumentedFields) {
        super("response", descriptors, attributes, ignoreUndocumentedFields);
    }

    @Override
    protected MediaType getContentType(Operation operation) {
        return operation.getResponse().getHeaders().getContentType();
    }

    @Override
    protected byte[] getContent(Operation operation) throws IOException {
        return operation.getResponse().getContent();
    }

    public static JsonResultResponseFieldsSnippet jsonResultResponseFields(FieldDescriptor... descriptors) {
        // add-default jsonResult-fields
        List<FieldDescriptor> list = new ArrayList<FieldDescriptor>(Arrays.asList(descriptors));

        list.add(0, fieldWithPath("result").type(JsonFieldType.NUMBER).description("API 통신 결과 - 0: 실패 / 1: 성공"));
        list.add(0, fieldWithPath("errorMessage").type(JsonFieldType.STRING)
                .description("result 결과가 0인 경우, 실패 원인에 대한 메세지"));
        list.add(0, fieldWithPath("data").description("API 결과"));
        return new JsonResultResponseFieldsSnippet(list, null, false);
    }

    public static FieldDescriptor jsonResultFieldWithPathByObject(String path) {
        return fieldWithPath("data." + path);
    }

    public static FieldDescriptor jsonResultFieldWithPathByArray(String path) {
        return fieldWithPath("data[]" + path);
    }
}