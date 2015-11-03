package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.SessionMsgMapper;
import org.courseAssist.model.SessionMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionMsgService {
	@Autowired
	private SessionMsgMapper smMapper;
	
	public List<SessionMsg> receivedMsg(int sid, int receiver, int start, int limit) {
		return smMapper.getReceivedMessages(sid, receiver, start, limit);
	}
	
	public void sendMsg(SessionMsg sm) {
		smMapper.sendMessage(sm);
	}
}
