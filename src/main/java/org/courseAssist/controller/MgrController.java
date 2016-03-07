package org.courseAssist.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.model.CourseTpl;
import org.courseAssist.model.Department;
import org.courseAssist.model.University;
import org.courseAssist.service.MgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MgrController {
	@Autowired
	public MgrService ms;
	
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
