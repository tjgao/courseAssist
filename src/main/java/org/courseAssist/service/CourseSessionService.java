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
		List<CourseSession> lcs = csMapper.getCourseSessionByUID(uid);
		for( CourseSession cs : lcs ) {
			cs.setLecturer(getLecturerNameBySID(cs.getSid()));
		}
		return lcs;
	}
	
	public int islecturer(int uid, int sid) {
		return csMapper.islecturer(uid, sid);
	}
	
	public CourseSession getCourseSession(int sid) {
		CourseSession cs = csMapper.getCourseSession(sid);
		User u = getLecturerBySID(cs.getSid());
		cs.setLecturer(u.getRealname());
		cs.setLecturerHeader(u.getHeadimg());
		cs.setUid(u.getId());
		return cs;
	}
	
	public String getLecturerNameBySID(int sid) {
		return csMapper.getLecturerNameBySID(sid);
	}
	
	public User getLecturerBySID(int sid) {
		return csMapper.getLecturerBySid(sid);
	}
}
