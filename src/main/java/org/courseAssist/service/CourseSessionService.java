package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.CourseSessionMapper;
import org.courseAssist.model.CourseSession;
import org.courseAssist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseSessionService {
	@Autowired
	CourseSessionMapper csMapper;
	public List<CourseSession> getCourseSessionByUID(int uid) {
		return csMapper.getCourseSessionByUID(uid);
	}
	
	public String getLecturerNameBySID(int sid) {
		return csMapper.getLecturerNameBySID(sid);
	}
	
	public User getLecturerBySID(int sid) {
		return csMapper.getLecturerBySid(sid);
	}
}
