package com.mobigen.datalake.metaData.manage;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/api/metadata/manage")
@RestController
@RequiredArgsConstructor
public class MetaDataManageController {
    private final MetaDataManageService metaDataManageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataManageController.class);

     /**
      * 메타 데이터 통합 관리 > 메타 데이터 통합 관리 대상 목록 조회
      *
      * @params Object       카테고리명
      * @return Object       메타 데이터 통합 관리 대상 목록 반환
      */
     @ResponseJsonResult
     @PostMapping("/list")
     public Object getMetaDataList(@RequestBody Map<String, Object> param) throws Exception {
      return metaDataManageService.getMetaDataManageList(param);
     }

}
