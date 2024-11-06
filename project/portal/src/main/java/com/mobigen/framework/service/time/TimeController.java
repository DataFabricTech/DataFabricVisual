package com.mobigen.framework.service.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobigen.framework.result.JsonResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/service/time")
public class TimeController {
	
	@Autowired
	private TimeService timeService;
	
	@RequestMapping(value = "/servertime")
	@ResponseBody
	public JsonResult serverTime() throws Exception {
		JsonResult js = new JsonResult();
		js.setData(timeService.getServerTime());
		log.debug("Current System Time: " + js.getData().toString());
		return js;
	}
	
	@RequestMapping(value = "/timeoffset")
	@ResponseBody
	public JsonResult timeOffset(String clientDateTime) throws Exception {
		JsonResult js = new JsonResult();
		js.setData(timeService.getTimeOffset(clientDateTime));
		log.debug("Current System Time Offset: " + js.getData().toString());
		return js;
	}
	
}
