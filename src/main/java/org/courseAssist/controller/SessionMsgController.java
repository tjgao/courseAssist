package org.courseAssist.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.courseAssist.model.MessageContent;
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

	@RequestMapping(value="/api/courseSession/messages/receive/{id}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> receiveMsg(@PathVariable("id") int id) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			List<SessionMsg> l = smService.receivedMsg(id);
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
	
	@RequestMapping(value="/api/courseSession/messages/loadnew/{id}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> loadnewMsg( HttpServletRequest req, 
			@PathVariable("id") int id) {
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			List<SessionMsg> l = smService.receiveNewMsg(uid, id);
			if( l.size() > 0 ) h.put("data", l);
			h.put("size", l.size());
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/messages/load/{id}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> loadMsg(@PathVariable("id") int id) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			SessionMsg m = smService.readMsg(id);
			if ( m != null ) {
				h.put("data", m);
				h.put("code", 0);
			} else {
				h.put("code", 1);
				h.put("msg", "此消息不存在！");
				logger.info("Trying to read msg {}, but it does not exist", id);
			}
		}catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/messages/loadold/{id}/{limit}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> loadoldMsg(HttpServletRequest req, 
			@PathVariable("id") int id, 
			@PathVariable("limit") int limit) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			List<SessionMsg> l = smService.receiveOldMsg(uid, id, limit);
			if( l.size() > 0 ) h.put("data", l);
			h.put("size", l.size());
			h.put("code", 0);
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}

	@RequestMapping(value="/api/courseSession/messages/receive/{start}/{limit}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String, Object> receivedMsg(HttpServletRequest req,  
			@PathVariable("start") int start, @PathVariable("limit") int limit) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			List<SessionMsg> l = smService.receivedMsg(uid, start, limit);
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
	
	@RequestMapping(value="/api/courseSession/messages/sendall", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> sendAll(
			@RequestParam(value="receiver") String receiver, HttpServletRequest req, 
			@RequestParam(value="title", required=false) String title, @RequestParam("content") String content) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		int uid = Integer.parseInt((String)req.getAttribute("uid"));
		try{
			content = new String(content.getBytes("ISO8859-1"), "utf-8");
			title = new String(title.getBytes("ISO8859-1"), "utf-8");
			String[] arr = receiver.split(",");
			title = ( title == null || title.isEmpty() ) ? "NO SUBJECT" : title;
			MessageContent mc = new MessageContent();
			mc.setTitle(title);
			mc.setContent(content);
			int mid = smService.storeMsg(mc);
			for( String ss : arr ) {
				int id = 0;
				try{
					id = Integer.parseInt(ss.trim());
				} catch(Exception e) {
					continue;
				}
				SessionMsg sm = new SessionMsg();
				sm.setReceiver(id);
				sm.setSender(uid);
				sm.setMid(mid);
				smService.sendMsg(sm);
				h.put("code", 0);
			}
		} catch(Exception e) {
			logger.info(e.toString());
			h.put("code", 2);
			h.put("msg", "操作中发生异常！");
		}
		return h;
	}
	
	@RequestMapping(value="/api/courseSession/messages/send/{receiver}", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> sendMsg( HttpServletRequest req, 
			@PathVariable("receiver") int receiver, @RequestParam(value="title", required=false) String title, 
			@RequestParam("content") String content) {
		int uid = (Integer)req.getAttribute("uid");
		HashMap<String, Object> h = new HashMap<String, Object>();
		try{
			title = ( title == null || title.isEmpty() ) ? "NO SUBJECT" : title;
			MessageContent mc = new MessageContent();
			mc.setTitle(title);
			mc.setContent(content);
			int mid = smService.storeMsg(mc);
			
			SessionMsg sm = new SessionMsg();
			sm.setReceiver(receiver);
			sm.setSender(uid);
			sm.setMid(mid);
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
