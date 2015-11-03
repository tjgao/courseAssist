package org.courseAssist.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.courseAssist.model.SessionMsg;
import org.courseAssist.service.SessionMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SessionMsgController {
	@Autowired
	private SessionMsgService smService;
	
	private static final Logger logger = LoggerFactory.getLogger(SessionMsgController.class);

	@RequestMapping(value="/api/courseSession/messages/{sid}/{start}/{limit}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> receivedMsg(HttpSession s, @PathVariable("sid") int sid, 
			@PathVariable("start") int start, @PathVariable("limit") int limit) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			Object o = s.getAttribute("USERID");
			List<SessionMsg> l = smService.receivedMsg(sid, (Integer)o, start, limit);
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
	
	@RequestMapping(value="/api/courseSession/messages/send/{sid}/{receiver}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> sendMsg(HttpSession s, @PathVariable("sid") int sid, 
			@PathVariable("receiver") int receiver, @RequestParam(value="title", required=false) String title, 
			@RequestParam("content") String content) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			Object o = s.getAttribute("USERID");
			SessionMsg sm = new SessionMsg();
			sm.setSid(sid);
			sm.setReceiver(receiver);
			sm.setSender((Integer)o);
			sm.setTitle((title == null || title.isEmpty()) ? "NO SUBJECT":title);
			sm.setContent(content);
			smService.sendMsg(sm);
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
}
