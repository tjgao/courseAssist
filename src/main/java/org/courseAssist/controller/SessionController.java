package org.courseAssist.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.model.CourseSession;
import org.courseAssist.model.User;
import org.courseAssist.service.CourseSessionService;
import org.courseAssist.service.UserService;
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
	
	@Autowired
	private UserService uService;
	
	private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
	
	@RequestMapping(value="/api/courseSession/query", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> courseSession(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			List<CourseSession> l = csService.getCourseSessionByUID(uid);
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
	
	@RequestMapping(value="/api/courseSession/querySession/{start}/{limit}", method=RequestMethod.GET) 
	public @ResponseBody HashMap<String, Object> courseSessionQuery(HttpServletRequest req, 
			@PathVariable("start") int start, @PathVariable("limit") int limit) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Integer uid = Integer.parseInt((String)req.getAttribute("uid"));
		String sname = req.getParameter("sname");
		if( sname != null && !sname.isEmpty())
			try {
				sname = new String(sname.getBytes("ISO8859-1"), "utf-8");
			} catch(Exception e) { sname = ""; }
		logger.debug("search string:" + sname);
		try{
			User u = uService.getUserById(uid);
			if( u == null ) throw new Exception();
			List<CourseSession> l = null;
			int sz = 0;
			if( u.getPriv() != 0 ) {
				l = csService.queryCourseSessionEx(sname, start, limit);
				sz = csService.countCourseSessionEx(sname);
			} else {
				l = csService.queryCourseSession(uid, sname, start, limit);
				sz = csService.countCourseSessionByUID(uid, sname);
			}
			h.put("data", l);
			h.put("count", sz);
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 1);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/priv/{sid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> courseSessionPriv(HttpServletRequest req, @PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			h.put("code", 0);
			h.put("lecturer", csService.islecturer(uid, sid));
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/{sid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> courseSession(@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			CourseSession cs = csService.getCourseSession(sid);
			h.put("code", 0);
			h.put("data", cs);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常!");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/lecturerName/{sid}", method=RequestMethod.GET) 
	public @ResponseBody HashMap<String, Object> sessionLecturerName(@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			String name = csService.getLecturerNameBySID(sid);
			h.put("code", 0);
			h.put("data", name);
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
			User u = csService.getLecturerBySID(sid); 
			h.put("code", 0);
			h.put("data", u);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
}
