package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.CourseSession;
import org.courseAssist.model.User;

public interface CourseSessionMapper {
	@Select("select a.sid,b.sdate,b.edate,c.name, c.tag, d.deptname, e.name from sessionUser as a,"
			+ "courseSession as b, courses as c, dept as d, universities as e where uid=#{id} and "
			+ "a.sid = b.id and b.courseId = c.id and d.id = c.deptid and e.id = d.uniid "
			+ "and now() > b.sdate and now() < b.edate")
	List<CourseSession> getCourseSessionByUID(@Param("id") int uid);
	
	@Select("select a.sid,b.sdate,b.edate,c.name, c.tag, d.deptname, e.name from sessionUser as a,"
			+ "courseSession as b, courses as c, dept as d, universities as e where uid=#{id} and "
			+ "a.sid = b.id and b.courseId = c.id and d.id = c.deptid and e.id = d.uniid "
			+ "and now() > b.sdate and now() < b.edate limit #{start}, #{limit}")
	List<CourseSession> getAllCourseSessionByUIDPage(@Param("id") int uid, @Param("start") int start, @Param("limit") int limit);
	
	@Select("select a.sid,b.sdate,b.edate,c.name, c.tag, d.deptname, e.name from sessionUser as a,"
			+ "courseSession as b, courses as c, dept as d, universities as e where uid=#{id} and "
			+ "a.sid = b.id and b.courseId = c.id and d.id = c.deptid and e.id = d.uniid "
			+ "and now() > b.sdate and now() < b.edate and c.name like #{sname} limit #{start}, #{limit}")
	List<CourseSession> queryCourseSessionByUIDPage(@Param("id") int uid, @Param("sname") String sname, @Param("start") int start, @Param("limit") int limit);
	
	@Select("select count(*) from sessionUser as a,"
			+ "courseSession as b, courses as c, dept as d, universities as e where uid=#{id} and "
			+ "a.sid = b.id and b.courseId = c.id and d.id = c.deptid and e.id = d.uniid "
			+ "and now() > b.sdate and now() < b.edate and c.name like #{sname}")
	int countCourseSessionByUID2(@Param("id") int uid, @Param("sname") String sname);
	

	@Select("select count(*) from sessionUser as a,"
			+ "courseSession as b, courses as c, dept as d, universities as e where uid=#{id} and "
			+ "a.sid = b.id and b.courseId = c.id and d.id = c.deptid and e.id = d.uniid "
			+ "and now() > b.sdate and now() < b.edate ")
	int countCourseSessionByUID(@Param("id") int uid);
	
	@Select("select b.id as sid,b.sdate,b.edate,c.name, c.tag, d.deptname, e.name from "
			+ "courseSession as b, courses as c, dept as d, universities as e where  b.courseId = c.id "
			+ "and d.id = c.deptid and e.id = d.uniid and now() > b.sdate and now() < b.edate and "
			+ "c.name like #{sname} limit #{start}, #{limit}")
	List<CourseSession> queryCourseSessionPage(@Param("sname") String sname, @Param("start") int start, @Param("limit") int limit);
	
	@Select("select count(*) from "
			+ "courseSession as b, courses as c, dept as d, universities as e where  b.courseId = c.id "
			+ "and d.id = c.deptid and e.id = d.uniid and now() > b.sdate and now() < b.edate and "
			+ "c.name like #{sname} ")	
	int countCourseSession2(@Param("sname") String sname);
	
	
	@Select("select count(*) from "
			+ "courseSession as b, courses as c, dept as d, universities as e where  b.courseId = c.id "
			+ "and d.id = c.deptid and e.id = d.uniid and now() > b.sdate and now() < b.edate")
	int countCourseSession();
	
	@Select("select b.id as sid,b.sdate,b.edate,c.name, c.tag, d.deptname, e.name from " 
			+ "courseSession as b, courses as c, dept as d, universities as e where  b.courseId = c.id  "
			+ " and d.id = c.deptid and e.id = d.uniid and now() > b.sdate and now() < b.edate limit #{start}, #{limit}") 
	List<CourseSession> getAllCourseSessionPage(@Param("start") int start, @Param("limit") int limit);
	
	
	@Select("select a.id as sid, b.name, a.sdate, a.edate,b.tag, c.deptname, d.name from "
			+ "courseSession as a, courses as b, dept as c, universities as d where "
			+ "a.courseId=b.id  and b.deptid = c.id and c.uniid = d.id and a.id=#{sid}")
	CourseSession getCourseSession(@Param("sid") int sid);
	
	@Select("select b.nickname from sessionUser as a, user as b "
			+ "where a.sid=#{id} and a.privilege=1 and a.uid = b.id")
	String getLecturerNameBySID(@Param("id") int sid);
	
	@Select("select b.* from sessionUser as a, user as b where a.sid=#{sid} and a.uid=b.id and a.privilege = 1")
	User getLecturerBySid(@Param("sid") int sid);
	
	@Select("select count(*) from sessionUser where uid=#{uid} and sid=#{sid} and privilege > 0")
	int islecturer(@Param("uid") int uid, @Param("sid") int sid);	
}
