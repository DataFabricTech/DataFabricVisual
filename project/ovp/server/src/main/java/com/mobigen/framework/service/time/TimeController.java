package com.mobigen.framework.service.time;

import com.mobigen.framework.result.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/service/time")
public class TimeController {
	private final TimeService timeService;

	@RequestMapping(value = "/servertime")
	public JsonResult serverTime() throws Exception {
		JsonResult js = new JsonResult();
		js.setData(timeService.getServerTime());
		log.debug("Current System Time: " + js.getData().toString());
		return js;
	}

	@RequestMapping(value = "/timeoffset")
	public JsonResult timeOffset(String clientDateTime) throws Exception {
		JsonResult js = new JsonResult();
		js.setData(timeService.getTimeOffset(clientDateTime));
		log.debug("Current System Time Offset: " + js.getData().toString());
		return js;
	}

}
