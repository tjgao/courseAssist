package org.courseAssist.controller;

import java.util.HashMap;
import java.util.List;

import org.courseAssist.model.CourseSession;
import org.courseAssist.service.CourseSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SessionController {
	@Autowired
	private CourseSessionService csService;
	
	private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
	
	@RequestMapping(value="/api/courseSession/query/{uid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> courseSession(@PathVariable("uid") int uid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			List<CourseSession> l = csService.getCourseSessionByUID(uid);
			h.put("code", 0);
			h.put("data", l);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/lecturer/{sid}", method=RequestMethod.GET) 
	public @ResponseBody HashMap<String, Object> sessionLecturer(@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			String name = csService.getLecturerBySID(sid);
			h.put("code", 0);
			h.put("data", name);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
}
