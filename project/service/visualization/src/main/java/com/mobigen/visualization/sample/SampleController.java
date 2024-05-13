package com.mobigen.visualization.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 샘플 컨트롤러 - 작동 확인 여부이므로 추후 삭제 필요
 */
@RestController
@RequestMapping("/api/sample")
@RequiredArgsConstructor
public class SampleController {
    private final SampleService sampleService;

    @GetMapping("/get")
    public String getSample() {
        return sampleService.getSample();
    }
}
