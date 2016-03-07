package org.courseAssist.controller;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.AppConfig;
import org.courseAssist.model.CourseSession;
import org.courseAssist.model.User;
import org.courseAssist.service.CourseSessionService;
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
	private CourseSessionService csService;	

	@Autowired
	UserService uService;
	
	@RequestMapping(value="/api/user/good", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> good(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Object id = req.getSession().getAttribute("USERID");
		if(  id != null ) {
			User u = uService.getUserById((Integer)id);
			h.put("code", 0);
			h.put("msg", "Success");
			h.put("nickname", u.getNickname());
			h.put("realname", u.getRealname());
		} else {
			h.put("code", 1);
			h.put("msg", "Fail");
		}
		return h;
	}
	
	@RequestMapping(value="/api/user/info", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> userinfo(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try {
			User u = uService.getUserById(uid);
			h.put("code", 0);
			h.put("email", u.getEmail());
			h.put("pid", u.getPid());
			h.put("mobile", u.getMobile());
			h.put("headimg", u.getHeadimg());
			h.put("nickname", u.getNickname());
			h.put("realname", u.getRealname());
			h.put("uid", u.getId());
			h.put("uniname", u.getUniname());
			h.put("uniid", u.getUniid());
			h.put("deptid", u.getDeptid());
			h.put("deptname", u.getDeptname());
		} catch(Exception e) {
			e.printStackTrace();
			h.put("code", 1);
			h.put("msg", "获取个人信息失败！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/user/chgpwd/{oldp}/{newp}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> chgpwd(HttpServletRequest req,
			@PathVariable("oldp") String oldp, @PathVariable("newp") String newp) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try {
			User u = uService.getUserById(uid);
			String pwd = u.getPwd();
			if (pwd == null || pwd.isEmpty()) {
				if (!oldp.equals(u.getPid())) {
					h.put("msg", "原密码输入有误！");
					h.put("code", 1);
					return h;
				}
			} else {
				if( !CommonUtils.md5(oldp).equals(pwd) ) {
					h.put("msg", "原密码输入有误！");
					h.put("code", 1);
					return h;
				}
			}
			newp = CommonUtils.md5(newp);
			uService.chgPasswordById((Integer) uid, newp);
			h.put("code", 0);
		} catch (Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "修改密码时发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/user/connected", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> connected(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			List<CourseSession> lcs = csService.getCourseSessionByUID(uid);
			ArrayList<HashMap<String,Object>> al = new ArrayList<HashMap<String, Object>>();
			for( CourseSession c : lcs ) {
				HashMap<String, Object> hh = new HashMap<String, Object>();
				hh.put("realname", c.getName());
				hh.put("sessionId", c.getSid());
				List<User> lu = uService.getUsersBySid(c.getSid());
				for( int i=0; i<lu.size(); i++ ) {
					if( lu.get(i).getId() == uid ) {
						lu.remove(i);
						break;
					}
				}
				hh.put("tree", lu);
				hh.put("numOfUsers", lu.size());
				al.add(hh);
			}
			h.put("data", al);
			h.put("count", al.size());
			h.put("code", 0);
		} catch(Exception e) {
			h.put("code", 1);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/user/logout", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> logout() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("code", 0);
		return h;
	}

	private String generateToken(int uid) {
		JwtBuilder b = Jwts.builder();
		return b.setIssuer("courseAssist").setIssuedAt(new Date()).setSubject(Integer.toString(uid))
			.signWith(SignatureAlgorithm.HS256, AppConfig.signingKey).compact();
	}
	
	@RequestMapping(value = "/api/user/login/{name}/{pwd}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> login(
			@PathVariable("name") String name, @PathVariable("pwd") String pwd) {
		try {
			name = new String(name.getBytes("ISO8859-1"), "utf-8");
		} catch (Exception e) {
			logger.info(e.toString());
			name = "";
		}
		HashMap<String, Object> h = new HashMap<String, Object>();
		String ppwd = CommonUtils.md5(pwd);
		User u = uService.getUserByNamePwd(name, ppwd);
		if (u == null) {
			u = uService.getUserByName(name);
			if (u == null) {
				h.put("code", 1);
				h.put("msg", "用户名或密码错误！");
			} else {
				ppwd = u.getPwd();
				if ((ppwd == null || ppwd.isEmpty()) && pwd.equals(u.getPid())) {
					h.put("code", 0);
					h.put("email", u.getEmail());
					h.put("pid", u.getPid());
					h.put("mobile", u.getMobile());
					h.put("headimg", u.getHeadimg());
					h.put("nickname", u.getNickname());
					h.put("realname", u.getRealname());
					h.put("uid", u.getId());
					h.put("uniname", u.getUniname());
					h.put("uniid", u.getUniid());
					h.put("deptid", u.getDeptid());
					h.put("deptname", u.getDeptname());
					h.put("token", generateToken(u.getId()));
				} else {
					h.put("code", 1);
					h.put("msg", "用户名或密码错误！");
				}
			}
		} else {
			h.put("code", 0);
			h.put("email", u.getEmail());
			h.put("pid", u.getPid());
			h.put("mobile", u.getMobile());
			h.put("headimg", u.getHeadimg());
			h.put("nickname", u.getNickname());
			h.put("realname", u.getRealname());
			h.put("uid", u.getId());
			h.put("uniname", u.getUniname());
			h.put("uniid", u.getUniid());
			h.put("deptid", u.getDeptid());
			h.put("deptname", u.getDeptname());
			h.put("token", generateToken(u.getId()));
		}
		return h;
	}
	
	@RequestMapping(value="/api/user/update", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> update(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		String realname = (String)req.getParameter("realname");
		String email = (String)req.getParameter("email");
		String mobile = (String)req.getParameter("mobile");
		try {
			realname = new String(realname.getBytes("ISO8859-1"), "utf-8");
		} catch (Exception e) {
			logger.info(e.toString());
			realname = "";
		}		
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try {
			uService.updateUserBasicInfo(uid, realname, mobile, email);
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 1);
			h.put("msg", "更新用户信息时发生异常！");
		}
		return h;
	}
}
