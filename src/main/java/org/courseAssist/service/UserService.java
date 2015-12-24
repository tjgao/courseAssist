package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.SessionMapper;
import org.courseAssist.mapper.UserMapper;
import org.courseAssist.model.User;
import org.courseAssist.model.UserExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	UserMapper uMapper;
	@Autowired
	SessionMapper sMapper;
	
	public User getUserByNamePwd(String name, String pwd) {
		return uMapper.getUserByNamePwd(name, pwd);
	}
	
	public User getUserByIdPwd(int id, String pwd) {
		return uMapper.getUserByIdPwd(id, pwd);
	}
	
	public User getUserById(int id) {
		return uMapper.getUserById(id);
	}
	
	public User getUserByName(String name) {
		return uMapper.getUserByName(name);
	}
	
	public void chgPasswordByName(String name, String pwd) {
		uMapper.chgPasswordByName(name, pwd);
	}

	public void chgPasswordById(int id, String pwd) {
		uMapper.chgPasswordById(id, pwd);
	}
	
	public User getUserBySidUid(int sid, int uid) {
		return uMapper.getUserBySidUid(sid, uid);
	}
	
	public List<User> getUsersBySid(int sid) {
		return uMapper.getUsersBySid(sid);
	}
	
	public List<UserExt> getUserExtsBySid(int sid) {
		List<UserExt> lu = uMapper.getUserExtBySid(sid);
		int shouldAttend, actualAttend;
		shouldAttend = sMapper.shouldAttend(sid);
		for( UserExt u : lu) {
			actualAttend = sMapper.actualAttend(sid,u.getId());
			u.setActualAttend(actualAttend);
			u.setShouldAttend(shouldAttend);
		}
		return lu;
	}
}
