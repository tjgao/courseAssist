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
	
	public List<SessionMsg> receivedMsg(int receiver, int start, int limit) {
		return smMapper.getReceivedMessages(receiver, start, limit);
	}

	public List<SessionMsg> receivedMsg(int receiver) {
		return smMapper.getAllReceivedMessages(receiver);
	}
	
	public List<SessionMsg> receiveNewMsg(int receiver, int id) {
		return smMapper.getReceiveNewMessages(id, receiver);
	}

	public List<SessionMsg> receiveOldMsg(int receiver, int id, int limit) {
		return smMapper.getReceiveOldMessages(id, receiver, limit);
	}

	public void sendMsg(SessionMsg sm) {
		smMapper.sendMessage(sm);
	}
	
	public SessionMsg readMsg(int id) {
		return smMapper.readMessage(id);
	}
}
