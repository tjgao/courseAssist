package org.courseAssist.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.courseAssist.model.User;
import org.courseAssist.service.UserService;
import org.courseAssist.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService uService;

	@RequestMapping(value="/api/user/good", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> good(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 0);
		if( req.getSession().getAttribute("USERID") != null ) 
			h.put("msg", "Success");
		else h.put("msg", "Fail");
		return h;
	}
	
	@RequestMapping(value="/api/user/chgpwd/{oldp}/{newp}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> chgpwd(HttpServletRequest req,
			@PathVariable("oldp") String oldp, @PathVariable("newp") String newp) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Object uid = req.getSession().getAttribute("USERID");
		if (null == uid) {
			h.put("msg", "尚未登录，无法修改密码！");
			h.put("code", 1);
			return h;
		}
		try {
			User u = uService.getUserById((Integer) uid);
			String pwd = u.getPwd();
			if (pwd == null || pwd.isEmpty()) {
				if (!oldp.equals(u.getPid())) {
					h.put("msg", "原密码输入有误！");
					h.put("code", 1);
					return h;
				}
			}
			uService.chgPasswordById((Integer) uid, newp);
			h.put("code", 0);
		} catch (Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "修改密码时发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/user/logout", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> logout(HttpSession s) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		s.removeAttribute("USERID");
		h.put("code", 0);
		return h;
	}

	@RequestMapping(value="/api/user/login/{name}/{pwd}", method=RequestMethod.GET)
	public  @ResponseBody HashMap<String, Object> login(HttpSession s, @PathVariable("name") String name, 
			@PathVariable("pwd") String pwd) {
		try{
			name = new String(name.getBytes("ISO8859-1"), "utf-8");
		} catch(Exception e) {
			logger.info(e.toString());
			name = "";
		}
		HashMap<String, Object> h = new HashMap<String, Object>();
		String ppwd = CommonUtils.md5(pwd);
		User u = uService.getUserByNamePwd(name, ppwd);
		if( u == null ) {
			User uu = uService.getUserByName(name);
			ppwd = uu.getPwd();
			if( ( ppwd == null || ppwd.isEmpty()) && pwd.equals(uu.getPid()) ) {
				h.put("code", 0);
				s.setAttribute("USERID", uu.getId());
			} else {
				h.put("code", 1);
				h.put("msg", "Fail");
			}
		} else {
			h.put("code", 0);
			s.setAttribute("USERID", u.getId());
		}
		return h;
	}
	
}
