package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.SessionWareMapper;
import org.courseAssist.model.SessionWare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionWareService {
	@Autowired
	SessionWareMapper swMapper;
	
	public List<SessionWare> getAllSessionWare(int sid) {
		return swMapper.getAllSessionWare(sid);
	}
	
	public SessionWare getSessionWare(int id) {
		return swMapper.getSessionWare(id);
	}
	
	public void uploadSessionWare(SessionWare s) {
		swMapper.uploadSessionWare(s);
	}
	
	public void deleteSessionWare(int id) {
		swMapper.deleteSessionWare(id);
	}
	
	public SessionWare getcw(int id) {
		return swMapper.getcw(id);
	}
}
