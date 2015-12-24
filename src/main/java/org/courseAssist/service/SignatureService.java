package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.SessionMapper;
import org.courseAssist.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SignatureService {
	@Autowired 
	private SessionMapper sMapper;
	
	public void createSignature(int sid, int uid, String sig) {
		sMapper.createSignature(uid, sid, sig);
	}
	
	public void signup(int sid, int uid, String sig) {
		sMapper.signup(uid, sid, sig);
	}
	
	public String signed(int sid, int uid, String sig) {
		return sMapper.signed(uid, sid, sig);
	}
	
	public String latestSig(int sid) {
		return sMapper.latestSig(sid);
	}
	
	public String sigtoday(int sid, int uid, String date) {
		return sMapper.sigtoday(sid, date, uid);
	}
	
	public List<Attendance> statistics(int sid) {
		return sMapper.statistics(sid);
	}
}
