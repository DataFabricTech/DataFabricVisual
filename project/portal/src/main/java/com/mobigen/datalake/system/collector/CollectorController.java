package com.mobigen.datalake.system.collector;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/admin/system/collector")
@Controller
@AllArgsConstructor
public class CollectorController {
    private final CollectorService collectorService;
    
    /**
     * 수집대상 목록 조회
     * @param searchType                검색 종류
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/collectorList")
    public Object getCollectorListInfo(@RequestParam("pageSize") int pageSize,
                                @RequestParam("pnum") int pnum,
                                @RequestParam("searchType") String searchType,
                                @RequestParam("searchWord") String searchWord) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchWord", searchWord);
        params.put("limit", String.valueOf(pageSize));
        params.put("offset", String.valueOf(pageSize * (pnum - 1)));

        return collectorService.getCollectorListInfo(params);
    }

    /**
     * 수집대상 시스템 ID 중복확인
     * @param system_id                시스템 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/systemIdDupCheck")
    public Object systemIdDupCheck(@RequestParam("system_id") String system_id) throws Exception {
        return collectorService.systemIdDupCheck(system_id);
    }

    /**
     * 수집대상 등록
     * @param params               수집대상 시스템
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/add")
    public Object addCollector(@RequestBody Map<String, Object> params) throws Exception {
        return collectorService.addCollector(params);
    }

    /**
     * 수집대상 수정
     * @param params               수집대상 시스템
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/update")
    public Object updateCollector(@RequestBody Map<String, Object> params) throws Exception {
        return collectorService.updateCollector(params);
    }

    /**
     * 수집대상 삭제
     * @param system_id                시스템 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/delete")
    public Object deleteCollector(@RequestParam("system_id") String system_id) throws Exception {
        return collectorService.deleteCollector(system_id);
    }

    /**
     * 수동 수집 실행
     * @param system_id                시스템 ID
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/directCollect")
    public Object directCollect(@RequestParam("system_id") String system_id) throws Exception {
        return collectorService.directCollect(system_id);
    }


}
