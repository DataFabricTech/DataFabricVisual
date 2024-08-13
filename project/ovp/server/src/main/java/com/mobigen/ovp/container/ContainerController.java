package com.mobigen.ovp.container;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("api/containers")
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
    public Object getContainersName(@PathVariable("id") String id) {
        return containerService.getContainersName(id);
    }
}
