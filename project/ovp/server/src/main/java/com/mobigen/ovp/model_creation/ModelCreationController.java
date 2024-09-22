package com.mobigen.ovp.model_creation;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "북마크 추가 오류")
    @PutMapping("/bookmark/add/{id}")
    public Object addBookMark(@PathVariable String id) throws Exception {
        return modelCreationService.addBookMark(id);
    }

    /**
     * 북마크 삭제
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "북마크 삭제 오류")
    @DeleteMapping("/bookmark/remove/{id}")
    public Object removeBookMark(@PathVariable String id) throws Exception {
        return modelCreationService.removeBookMark(id);
    }

    /**
     * 데이터 모델 생성 > 추가모달 > MY 리스트
     *
     * @param params
     * @return
     */
    @ResponseJsonResult(errorMessage = "My 리스트 조회 오류")
    @GetMapping("/my-list")
    public Object getMyList(@RequestParam MultiValueMap<String, String> params ) {
        return modelCreationService.getMyList(params);
    }

    /**
     * 데이터 모델 생성 > 추가모달 > 리스트
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "데이터 모델 목록 조회 오류")
    @GetMapping("/list")
    public Object getSearchList(@RequestParam MultiValueMap<String, String> params) throws Exception {
        return modelCreationService.getSearchList(params);
    }

    /**
     * 데이터 모델 생성 > 저장모달 > 이름 중복 체크
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "데이터 모델 이름 중복 체크 오류")
    @PostMapping("/validation-name")
    public Object checkDuplicateModelName(@RequestBody Map<String, Object> param) throws Exception {
        return modelCreationService.checkDuplicateModelName(param);
    }

    /**
     * 데이터 모델 생성 > 저장모달 > 모델 저장
     *
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult(errorMessage = "데이터 모델 저장 오류")
    @PostMapping("/save")
    public Object saveModel(@RequestBody Map<String, Object> param) throws Exception {
        return modelCreationService.saveModel(param);
    }

}
