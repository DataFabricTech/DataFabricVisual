package com.mobigen.datalake.system.recommend;

import com.mobigen.framework.result.annotation.ResponseJsonResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/api/recommend")
@Controller
@AllArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 추천검색어 목록 조회
     * @return                      추천 검색어 목록
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/search")
    public Object getRecommendSearchList()  throws Exception {
        return recommendService.getRecommendSearchList();
    }

    /**
     * 추천검색어 저장
     * @param param                 추천 검색어 목록
     * @return                      추천 검색어 저장 결과
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/search/save")
    @ResponseBody
    public Object saveRecommendMetaList(@RequestBody Map<String, Object> param) throws Exception {
        return recommendService.saveRecommendSearchList(param);
    }
}
