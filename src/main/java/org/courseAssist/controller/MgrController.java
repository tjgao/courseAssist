package org.courseAssist.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.model.CourseTpl;
import org.courseAssist.model.Department;
import org.courseAssist.model.University;
import org.courseAssist.model.User;
import org.courseAssist.service.MgrService;
import org.courseAssist.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;

@Controller
public class MgrController {
	@Autowired
	public MgrService ms;
	
	@Autowired
	public UserService us;

	private static final Logger logger = (Logger) LoggerFactory.getLogger(MgrController.class);	

	@RequestMapping("/api/mgr/listuni")
	public @ResponseBody HashMap<String, Object> listuni(HttpServletRequest req) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			List<University> lu = ms.listUni();
			h.put("data", lu);
			h.put("code", 0);
		} catch(Exception e) {
			h.put("data", new ArrayList<University>());
			h.put("code", 1);
		}
		return h;
	}
	
	@RequestMapping("/api/mgr/listdept/{uniid}")
	public @ResponseBody HashMap<String, Object> listdept(HttpServletRequest req, @PathVariable("uniid") int uniid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			List<Department> ld = ms.listDept(uniid);
			h.put("data", ld);
			h.put("code", 0);
		} catch(Exception e) {
			h.put("data", new ArrayList<Department>());
			h.put("code", 1);
		}
		return h;		
	}
	
	@RequestMapping("/api/mgr/updatedept")
	public @ResponseBody HashMap<String, Object> updatedept( HttpServletRequest req, @RequestBody Department d) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			User u = us.getUserById(uid);
			if( u.getPriv() == 0 ) {
				h.put("code", 2);
				h.put("msg", "无权限执行此操作！");
				return h;
			}
			if( d.getId() != 0 && d.getUniid() != 0 )
				ms.updateDept(d.getId(), d.getDeptname());
			else if( d.getId() == 0 && d.getUniid() != 0 ) 
				ms.insertDept(d.getUniid(), d.getDeptname());
			else {
				h.put("code", 1);
				h.put("msg", "错误的参数类型！");
				return h;
			}
			h.put("code", 0);
		} catch(Exception e) {
			h.put("code", 1);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	
	@RequestMapping("/api/mgr/listcoursetpl/{uniid}")
	public @ResponseBody HashMap<String, Object> listCourseTpl(HttpServletRequest req, @PathVariable("uniid") int uniid,
			@RequestParam("deptId") int deptId, 
			@RequestParam("start") int start, @RequestParam("limit") int limit) {
		HashMap<String, Object> h = new HashMap<String, Object>();

		try {
			List<CourseTpl> lc = null;
			int sz = 0;
			if( deptId == 0 ) {
				lc = ms.listCourses(uniid, start, limit);
				sz = ms.countCourses(uniid);
			} else {
				lc = ms.listCoursesByDept(uniid, deptId, start, limit);
				sz = ms.countCoursesByDept(uniid, deptId);
			}
			h.put("data", lc);
			h.put("count", sz);
			h.put("code", 0);
		} catch(Exception e) {
			h.put("data", new ArrayList<CourseTpl>());
			h.put("count", 0);
			h.put("code", 1);
		}
		return h;
	}
}
