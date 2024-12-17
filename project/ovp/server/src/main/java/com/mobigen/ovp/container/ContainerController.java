package com.mobigen.ovp.container;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/api/containers")
@RestController
@RequiredArgsConstructor
public class ContainerController {
    private final ContainerService containerService;

    /**
     * 탐색 - preview - type storage
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "스토리지 미리보기 조회 오류")
    @GetMapping("/{id}")
    public Object getContainersObject(@PathVariable("id") String id) {
        return containerService.getContainersObject(id);
    }

    /**
     * 비정형 메타데이터(확장자 hwp, hwpx, doc, docx) 조회
     *
     * @return
     */
    @ResponseJsonResult(errorMessage = "비정형 메타데이터 조회 오류")
    @GetMapping("/name/{fqn}")
    public Object getContainersMetadata(@PathVariable("fqn") String fqn) {
        return containerService.getContainersMetadata(fqn);
    }

}
