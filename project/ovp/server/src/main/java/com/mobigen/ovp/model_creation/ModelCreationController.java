package com.mobigen.ovp.model_creation;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/api/creation")
@RestController
@RequiredArgsConstructor
public class ModelCreationController {
    private final ModelCreationService modelCreationService;

    /**
     * 쿼리 실행
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/query/execute")
    public Object executeQuery(@RequestBody Map<String, Object> param) throws Exception {
        return modelCreationService.executeQuery(param);
    }

    /**
     * 북마크 추가
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "북마크 추가 오류")
    @PostMapping("/bookmark/add")
    public Object addBookMark(@RequestBody Map<String, Object> param) throws Exception {
        return modelCreationService.addBookMark(param);
    }

    /**
     * 북마크 삭제
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "북마크 삭제 오류")
    @PostMapping("/bookmark/remove")
    public Object removeBookMark(@RequestBody Map<String, Object> param) throws Exception {
        return modelCreationService.removeBookMark(param);
    }



}

