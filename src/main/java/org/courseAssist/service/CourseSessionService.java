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
	
	public List<CourseSession> getCourseSessionByUIDPage(int uid, int start, int limit) {
		List<CourseSession> lcs = csMapper.getAllCourseSessionByUIDPage(uid, start, limit);
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
	
	public List<CourseSession> queryCourseSessionEx(String sname, int start, int limit) {
		if( sname == null || sname.isEmpty() )
			return csMapper.getAllCourseSessionPage(start, limit);
		else {
			sname = "%" + sname + "%";
			return csMapper.queryCourseSessionPage(sname, start, limit);
		}
	}
	
	public int countCourseSessionEx(String sname) {
		if( sname == null || sname.isEmpty() )
			return csMapper.countCourseSession();
		else {
			sname = "%" + sname + "%";
			return csMapper.countCourseSession2(sname);
		}
	}

	public List<CourseSession> queryCourseSession(int uid, String sname, int start, int limit ) {
		if( sname != null && !sname.isEmpty()) {
			sname = "%" + sname + "%";
			return csMapper.queryCourseSessionByUIDPage(uid, sname, start, limit);
		}
		else
			return csMapper.getAllCourseSessionByUIDPage(uid, start, limit);
	}
	
	public int countCourseSessionByUID(int uid, String sname) {
		if( sname != null && !sname.isEmpty()) {
			sname = "%" + sname + "%";
			return csMapper.countCourseSessionByUID2(uid, sname);
		}
		else
			return csMapper.countCourseSessionByUID(uid);
	}
}
