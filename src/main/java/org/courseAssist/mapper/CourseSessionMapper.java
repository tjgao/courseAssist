package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.CourseSession;
import org.courseAssist.model.User;

public interface CourseSessionMapper {
	@Select("select a.sid,b.sdate,b.edate,c.name, d.deptname, e.name from sessionUser as a,"
			+ "courseSession as b, courses as c, dept as d, universities as e where uid=#{id} and "
			+ "a.sid = b.id and b.courseId = c.id and d.id = c.deptid and e.id = d.uniid "
			+ "and now() > b.sdate and now() < b.edate")
	List<CourseSession> getCourseSessionByUID(@Param("id") int uid);
	
	@Select("select b.nickname from sessionUser as a, user as b "
			+ "where a.sid=#{id} and a.privilege=1 and a.uid = b.id")
	String getLecturerNameBySID(@Param("id") int sid);
	
	@Select("select b.* from sessionUser as a, user as b where a.sid=#{sid} and a.uid=b.id and a.privilege = 1;")
	User getLecturerBySid(@Param("sid") int sid);
}
