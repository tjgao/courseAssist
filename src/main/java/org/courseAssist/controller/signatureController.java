package org.courseAssist.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.courseAssist.model.Attendance;
import org.courseAssist.model.User;
import org.courseAssist.model.UserExt;
import org.courseAssist.service.CourseSessionService;
import org.courseAssist.service.SignatureService;
import org.courseAssist.service.UserService;
import org.courseAssist.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class signatureController {
	@Autowired
	private SignatureService sService;
	
	@Autowired
	private UserService uService;

	@Autowired
	private CourseSessionService csService;
	
	private static final Logger logger = LoggerFactory.getLogger(signatureController.class);
	
	@RequestMapping(value="/api/signature/statistics/{sid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> statistics(@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			List<UserExt> lu = uService.getUserExtsBySid(sid);
			h.put("data", lu);
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		/*
		try{
			List<Attendance> l = sService.statistics(sid);
			h.put("data", l);
			h.put("size", l.size());
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		*/
		return h;
	}
	
	@RequestMapping(value="/api/signature/create/{sid}/{sig}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> createSignature(HttpServletRequest req, @PathVariable("sid") int sid,
			@PathVariable("sig") String sig) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			int uid = Integer.parseInt((String) req.getAttribute("uid"));
			User u = uService.getUserById(uid);
			String s = generateSignature(uid, u.getName());
			if( s.equals(sig)) {
				sService.createSignature(sid, uid, sig);
				h.put("code", 0);
				h.put("data", sig);
			}
			else {
				h.put("code", 1);
				h.put("msg", "考勤码验证不正确！");
			}
		} catch (Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/signature/get/{sid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> getSignature(HttpServletRequest req, @PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			int uid = Integer.parseInt((String) req.getAttribute("uid"));
			User u = uService.getUserById(uid);
			User lecturer = csService.getLecturerBySID(sid);
			if (u.getId() != lecturer.getId()) {
				h.put("code", 1);
				h.put("data", "权限不足无法操作！");
			} else {
				String sig = generateSignature(uid, u.getName());
				h.put("code", 0);
				h.put("data", sig);
			}
		} catch (Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value = "/api/signature/signup/{sid}/{sig}", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> signup(HttpServletRequest req,
			@PathVariable("sid") int sid, @PathVariable("sig") String sig) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			int uid = Integer.parseInt((String) req.getAttribute("uid"));
			User u = uService.getUserBySidUid(sid, uid);
			logger.debug("sig = " + sig);
			if (u == null) {
				h.put("code", 1);
				h.put("msg", "无法操作，此用户并未注册此课程！");
			} else {
				User lecturer = csService.getLecturerBySID(sid);
				if( !generateSignature(lecturer.getId(), lecturer.getName()).equals(sig)) {
					h.put("code", 1);
					h.put("msg", "签名不符！");
					return h;
				}
				String sigtoday = sService.sigtoday(sid, lecturer.getId(), today());
				if (lecturer.getId() == uid) {
					// scanned by the teacher, validate the signature
					if (sigtoday == null || sigtoday.isEmpty()) {
						sService.createSignature(sid, uid, sig);
					}
				} else {
					// make sure the sig is correct
					if (!sig.equals(sigtoday)) {
						h.put("code", 1);
						h.put("msg", "签名不符！");
						return h;
					}
					sService.signup(sid, uid, sig);
				}
				h.put("code", 0);
			}
		} catch (Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	private String today() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		return sdf.format(d);
	}

	private String generateSignature(int uid, String name) {
		return CommonUtils.md5(name.toUpperCase() + uid + today() + "!");
	}
	
	
	@RequestMapping(value = "/api/signature/signed/{sid}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> signed(HttpServletRequest req,
			@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String) req.getAttribute("uid"));
		try {
			User lecturer = csService.getLecturerBySID(sid);
			if (lecturer == null) {
				h.put("code", 1);
				h.put("msg", "这门课没有任课老师!");
			} else {
				String sig = generateSignature(lecturer.getId(), lecturer.getName());
				String ret = sService.signed(sid, uid, sig);
				h.put("code", 0);
				if (sig.equals(ret)) {
					h.put("data", "true");
				} else
					h.put("data", "false");
			}
		} catch (Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常!");
		}
		return h;
	}
}
