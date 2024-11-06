package com.mobigen.datalake.system.tag;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/admin/system/tagManager")
@Controller
@AllArgsConstructor

public class TagManageController {
  private final TagManageService tagManageService;
  

  /**
   * Tag 추가
   * @param param
   * @return
   * @throws Exception
   */
  @ResponseJsonResult
  @PostMapping("/insert")
  @ResponseBody
  public Object insertTagInfo(@RequestBody Map<String, String> param, HttpServletRequest request) throws Exception {
      return tagManageService.insertTagInfo(param, request);
  }

  /**
     * Tag 목록 조회
     * @param pageSize
     * @param pnum
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/list")
    public Object getUserList(@RequestParam("pageSize") int pageSize, @RequestParam("pnum") int pnum) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("limit", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));

        int total = tagManageService.getTagListTotal(param);
        
        List<Map> list = tagManageService.getTagList(param);
        Map resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", total);

        return resultMap;
    }

  /**
   * Tag 삭제
   * @param param
   * @return
   * @throws Exception
   */
  @ResponseJsonResult
  @PostMapping("/delete")
  @ResponseBody
  public Object deleteTagInfo(@RequestBody Map<String, String> param) throws Exception {
      return tagManageService.deleteTagInfo(param);
  }

  /**
   * Tag 매핑
   * @param param
   * @return
   * @throws Exception
   */
  @ResponseJsonResult
  @PostMapping("/map")
  @ResponseBody
  public Object mapTagInfoPost(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
      return tagManageService.mapLabels(param, request);
  }

  /**
   * Tag 매핑
   * @param param
   * @return
   * @throws Exception
   */
  @ResponseJsonResult
  @PutMapping("/map")
  @ResponseBody
  public Object mapTagInfoPut(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
      return tagManageService.mapLabels(param, request);
  }
}
