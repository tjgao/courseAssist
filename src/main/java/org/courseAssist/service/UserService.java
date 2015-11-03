package org.courseAssist.service;

import org.courseAssist.mapper.UserMapper;
import org.courseAssist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	UserMapper uMapper;
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
}
