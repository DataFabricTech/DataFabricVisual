package com.mobigen.datalake.system.mailTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobigen.framework.result.annotation.ResponseJsonResult;

@Slf4j
@RequestMapping("/api/admin/system/mailTemplate")
@Controller
@AllArgsConstructor
public class MailTemplateController {

    private final MailTemplateService mailTemplateService;

    /**
     * 메일 템플릿 - 조회
     * 
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/getMailTemplateList")
    public Object getMailTemplateList(@RequestParam int pageSize, @RequestParam int pnum,
            @RequestParam String searchType, @RequestParam String searchWord)
            throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("searchType", searchType);
        param.put("searchWord", searchWord);
        param.put("pageSize", String.valueOf(pageSize));
        param.put("offset", String.valueOf(pageSize * (pnum - 1)));

        return mailTemplateService.getMailTemplateList(param);
    }

    /**
     * 메일 템플릿 - 상세 조회
     * 
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @GetMapping("/getMailTemplateInfo")
    public Object getMailTemplateInfo(@RequestParam String template_id) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("template_id", template_id);

        return mailTemplateService.getMailTemplateInfo(param);
    }

    /**
     * 메일 템플릿 - 저장
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/saveMailTemplate")
    @ResponseBody
    public Object saveMailTemplate(@RequestBody Map<String, Object> param) throws Exception {
        return mailTemplateService.saveMailTemplate(param);
    }

    /**
     * 메일 템플릿 - 삭제
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/deleteMailTemplate")
    @ResponseBody
    public Object deleteMailTemplate(@RequestBody Map<String, Object> param) throws Exception {
        return mailTemplateService.deleteMailTemplate(param);
    }

    /**
     * 메일 템플릿 - 메일 발송
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @ResponseJsonResult
    @PostMapping("/sendMailWithTemplate")
    @ResponseBody
    public Object sendMailWithTemplate(@RequestBody Map<String, Object> param) throws Exception {
        // param.get("variables")가 null인 경우 빈 HashMap을 생성
        Map<String, Object> variables = (param.get("variables") == null) ? new HashMap<>()
                : (Map<String, Object>) param.get("variables");

        // sendMail 메소드 호출
        return mailTemplateService.sendMail(param.get("template_id").toString(), variables);
    }
}
