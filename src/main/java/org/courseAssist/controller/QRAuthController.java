package org.courseAssist.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.AppConfig;
import org.courseAssist.model.QRAuth;
import org.courseAssist.model.User;
import org.courseAssist.service.CourseSessionService;
import org.courseAssist.service.QRAuthService;
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
public class QRAuthController {
	@Autowired
	private QRAuthService qService;
	
	@Autowired
	private CourseSessionService csService;
	
	@Autowired
	private UserService uService;
	
	private static final Logger logger = LoggerFactory.getLogger(QRAuthController.class);

	@RequestMapping(value="/api/qr/create/{ip}/{port}/{code}", method=RequestMethod.POST) 
	public @ResponseBody HashMap<String, Object> qrcreate(@PathVariable("ip") String ip, @PathVariable("port") int port,
			@PathVariable("code") String code)	{
		logger.debug("ip: {}  port: {} ", ip, Integer.toString(port));
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			QRAuth q = new QRAuth();
			q.setCode(code);
			q.setIp(ip);
			q.setPort(port);
			qService.insertQRAuth(q);
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中出现异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/qr/scan/{code}/{sid}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> qrscan(HttpServletRequest req, @PathVariable("code") String code, 
			@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String) req.getAttribute("uid"));
		try{
			if( csService.islecturer(uid, sid) == 0 ) {
				h.put("code", 1);
				h.put("msg", "非该课程任课老师，无法获得控制权！");
				return h;
			}
			final String header = req.getHeader("Authorization");
			QRAuth q = new QRAuth();
			q.setTime(new Date());
			q.setUid(uid);
			q.setSid(sid);
			q.setToken(CommonUtils.md5(header));
			q.setCode(code);
			qService.updateQRAuthByCode(q);
			h.put("code", 0);
			h.put("token", q.getToken());
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("msg", "操作中发生异常！");
			h.put("code", 2);
		}
		return h;
	}
	
	@RequestMapping(value="/api/qr/clean/{code}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> qrclean(@PathVariable("code") String code) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			qService.cleanQRAuth(code);
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("msg", "操作中发生异常！");
			h.put("code", 2);
		}
		return h;
	}
	
	@RequestMapping(value="/api/qr/wait/{code}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> qrwait(@PathVariable("code") String code) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 1);
		QRAuth q = qService.getQRAuthByCode(code);
		if( q.getToken() != null )  {
			h.put("code", 0);
			h.put("token", q.getToken());
			h.put("time", q.getTime());
			h.put("sid", q.getSid());
			h.put("uid", q.getUid());
			h.put("authid", q.getId());
		}
		return h;
	}
	
	/*
	 * It returns admin token to the user who scan the qr code
	 * and returns access token to other users
	 */
	@RequestMapping(value = "/api/qr/list/{sid}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> qrlist(HttpServletRequest req,
			@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			int uid = Integer.parseInt((String) req.getAttribute("uid"));
			AppConfig cfg = AppConfig.getConfig();
			int seconds = (Integer) cfg.get(AppConfig.bookingTime);
			QRAuth q = qService.getQRAuthBySid(sid, seconds);
			if( q != null ) {
				if( q.getUid() != uid )
					q.setToken(CommonUtils.md5(q.getToken()));
			}
			h.put("code", 0);
			h.put("data", q);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
}
