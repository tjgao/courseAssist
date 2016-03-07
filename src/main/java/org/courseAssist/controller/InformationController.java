package org.courseAssist.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.model.Information;
import org.courseAssist.model.User;
import org.courseAssist.service.InformationService;
import org.courseAssist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class InformationController {
	@Autowired
	private InformationService iService;
	@Autowired
	private UserService uService;
	
	private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
	
	@RequestMapping(value="/api/information/delete/{pid}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> deleteInfo(HttpServletRequest req, @PathVariable("pid") int pid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try {
			User u = uService.getUserById(uid) ;
			if( u.getPriv() == 0 ) {
				h.put("code", 1);
				h.put("msg","权限不足");
				return h;
			}
			iService.deleteInfo(pid);
			h.put("code", 0);
		} catch(Exception e) {
			h.put("code", 1);
			h.put("msg", "发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/information/insert", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> insertInfo(HttpServletRequest req,
			@RequestBody Information i) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try {
			User u = uService.getUserById(uid) ;
			if( u.getPriv() == 0 ) {
				h.put("code", 1);
				h.put("msg","权限不足");
				return h;
			}
			iService.insertInfo(i);
			h.put("code", 0);
		} catch(Exception e) {
			logger.debug(e.toString());
			h.put("code", 1);
			h.put("msg", "发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/information/update/{pid}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> updateInfo(HttpServletRequest req,
			@PathVariable("pid") int pid,
			@RequestBody Information i ) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try {
			User u = uService.getUserById(uid) ;
			if( u.getPriv() == 0 ) {
				h.put("code", 1);
				h.put("msg","权限不足");
				return h;
			}
			i.setId(pid);
			iService.updateInfo(i);
			h.put("code", 0);
		} catch(Exception e) {
			logger.debug(e.toString());
			h.put("code", 1);
			h.put("msg", "发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/information/listAll", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> listAll(HttpServletRequest req, 
			@RequestParam("uniid") int uniid,
			@RequestParam(value="deptid", required=false) Integer deptid,
			@RequestParam(value="type", required=false) Integer type,
			@RequestParam("start") int start,
			@RequestParam("limit") int limit
			){
		HashMap<String,Object> h = new HashMap<String,Object>();
		try{
			List<Information> l = null;
			int count = 0;
			if( deptid == null || deptid == 0 ) {
				l = iService.listInfo2(uniid, start, limit);
				count = iService.countInfo2(uniid);
			}
			else {
				l = iService.listInfo(uniid, deptid, start, limit);
				count = iService.countInfo(uniid, deptid);
			}
			h.put("code", 0);
			h.put("data", l);
			h.put("count", count);
		} catch(Exception e) {
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/information/getinfo/{iid}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> getinfo(HttpServletRequest req, @PathVariable("iid") int iid) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			Information i = iService.getinfo(iid);
			h.put("data", i);
			h.put("code", 0);
		} catch(Exception e) {
			h.put("code", 1);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}

}
