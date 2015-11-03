package org.courseAssist.controller;

import java.util.HashMap;
import java.util.List;

import org.courseAssist.model.Information;
import org.courseAssist.service.InformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InformationController {
	@Autowired
	private InformationService iService;
	
	private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
	
	@RequestMapping(value="/api/information/{uniid}/{deptid}/{type}/{start}/{limit}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> getInformation(@PathVariable("uniid") int uniid,
			@PathVariable("deptid") int deptid, @PathVariable("type") int type, 
			@PathVariable("start") int start, @PathVariable("limit") int limit) {
		HashMap<String,Object> h = new HashMap<String,Object>();
		try{
			List<Information> l = iService.getInformation(uniid, deptid, type, start, limit);
			h.put("code", 0);
			h.put("data", l);
			h.put("count", l.size());
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
}
