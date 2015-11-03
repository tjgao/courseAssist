package org.courseAssist.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.courseAssist.model.Attendance;
import org.courseAssist.model.User;
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
			List<Attendance> l = sService.statistics(sid);
			h.put("data", l);
			h.put("size", l.size());
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/signature/create/{sid}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> createSignature(HttpSession session, @PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			Object o = session.getAttribute("USERID");
			if( o == null ) {
				h.put("code", 1);
				h.put("msg", "未登录，无法操作！");
			} else {
				User u = uService.getUserById((Integer)o);
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String sig = CommonUtils.md5(u.getPwd() + (Integer)o + sdf.format(d));
				sService.createSignature(sid, (Integer)o, sig);
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
	
	@RequestMapping(value="/api/signature/signup/{sid}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> signup(HttpSession session, @PathVariable("sid") int sid, @PathVariable("sig") String sig) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			Object o = session.getAttribute("USERID");
			if( o == null ) {
				h.put("code", 1);
				h.put("msg", "未登录，无法操作！");
			} else {
				User u = uService.getUserBySidUid(sid, (Integer)o);
				if( u == null ) {
					h.put("code", 1);
					h.put("msg", "无法操作，此用户并未注册此课程！");
				} else {
					//make sure the sig is correct
					String s = sService.latestSig(sid);
					if( !sig.equals(s) ) {
						h.put("code", 1);
						h.put("msg", "签名不符！");
						return h;
					}
					sService.signup(sid, (Integer)o, sig);
					h.put("code", 0);
				}
			}
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/signature/signed", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> signed(HttpSession session, @PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String,Object>();
		Object o = session.getAttribute("USERID");
		if( o == null ) {
			h.put("code", 1);
			h.put("msg", "未登录，无法操作！");
			return h;
		}
		try{
			User lecturer = csService.getLecturerBySID(sid);
			if( lecturer == null ) {
				h.put("code", 1);
				h.put("msg", "这门课没有任课老师!");
			} else {
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String sig = CommonUtils.md5(lecturer.getPwd() + lecturer.getId() + sdf.format(d));
				String ret = sService.signed(sid, (Integer)o, sig);
				h.put("code", 0);
				if( sig.equals(ret)) {
					h.put("data", "true");
				} else h.put("data", "false");
			}
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常!");
		}
		return h;
	}
}
