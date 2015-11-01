package org.courseAssist.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.AppConfig;
import org.courseAssist.model.QRAuth;
import org.courseAssist.model.User;
import org.courseAssist.service.QRAuthService;
import org.courseAssist.service.UserService;
import org.courseAssist.utils.CommonUtils;
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
	private UserService uService;
	
	@RequestMapping(value="/api/qr/create/{ip}/{port}/{code}", method=RequestMethod.POST) 
	public @ResponseBody HashMap<String, Object> qrcreate(@PathVariable("ip") String ip, @PathVariable("port") int port,
			@PathVariable("code") String code)	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			long lip = CommonUtils.ip2int(ip);
			QRAuth q = new QRAuth();
			q.setCode(code);
			q.setIp(lip);
			q.setPort(port);
			qService.insertQRAuth(q);
			h.put("code", 0);
		} catch(Exception e) {
			h.put("code", 2);
			h.put("msg", "操作中出现异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/qr/scan/{code}/{sid}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> qrscan(HttpServletRequest req, @PathVariable("code") String code, 
			@PathVariable("sid") int sid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Object o = req.getSession().getAttribute("USERID");
		if( o == null) {
			h.put("msg", "未登录，无法操作！");
			h.put("code", 1);
			return h;
		}
		try{
			User u = uService.getUserById((Integer)o);
			QRAuth q = new QRAuth();
			q.setTime(new Date());
			q.setUid((Integer)o);
			q.setSid(sid);
			q.setToken(CommonUtils.md5(q.getTime().toString() + u.getPwd()));
			qService.updateQRAuthByCode(q);
			h.put("code", 0);
			h.put("token", q.getToken());
		} catch(Exception e) {
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
			h.put("msg", "操作中发生异常！");
			h.put("code", 2);
		}
		return h;
	}
	
	@RequestMapping(value="/api/qr/wait/{code}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> qrwait(@PathVariable("code") String code) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 0);
		QRAuth q = qService.getQRAuthByCode(code);
		if( q.getToken() != null )  {
			h.put("token", q.getToken());
			h.put("time", q.getTime());
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
		h.put("code", 0);
		try {
			Object o = req.getSession().getAttribute("USERID");
			if( o == null ) {
				h.put("code", 1);
				h.put("msg", "未登录，无法操作！");
				return h;
			}
			User u = uService.getUserById((Integer)o);
			AppConfig cfg = AppConfig.getConfig();
			int seconds = (Integer) cfg.get(AppConfig.bookingTime);
			List<QRAuth> l = qService.getQRAuthBySid(sid, seconds);
			if (l.size() == 0)
				return h;
			for (QRAuth a : l) {
				if( u.getId() != a.getUid())
					a.setToken(CommonUtils.md5(a.getToken()));
			}
			h.put("data", l);
		} catch (Exception e) {
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
}
