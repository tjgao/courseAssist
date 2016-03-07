package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.Attendance;

public interface SessionMapper {
	@Insert("insert into SessionCounter (uid, sid, signature, time) values (#{uid}, #{sid}, #{sig}, now())")
	void createSignature(@Param("uid") int uid, @Param("sid") int sid, @Param("sig") String sig);
	
	@Insert("insert into SessionAttendance (uid, sid, signature, time) values (#{uid},#{sid},#{sig},now())")
	void signup(@Param("uid") int uid, @Param("sid") int sid, @Param("sig") String sig);
	
	@Select("select signature from SessionAttendance where sid=#{sid} and uid=#{uid} "
			+ "and signature=#{sig} and date(time)=date(now()) order by id desc limit 1")
	String signed(@Param("uid") int uid, @Param("sid") int sid, @Param("sig") String sig);
	
	@Select("select signature from sessionCounter where sid=#{sid} order by id desc limit 1")
	String latestSig(@Param("sid") int sid);
	
	@Select("select signature from sessionCounter where sid=#{sid} and date(time)=#{date} and uid=#{uid} order by id desc limit 1")
	String sigtoday(@Param("sid") int sid, @Param("date") String date, @Param("uid") int uid);
	
	@Select("select a.uid as uid, c.realname as name, d.deptname as deptname, a.sid as sid, e.count as count,"
			+ "count(*) as attend from sessionAttendance as a, (select uid from sessionuser where privilege=0) as b,"
			+ "user as c, dept as d, (select count(*) as count from sessioncounter where sid=#{sid}) as e "
			+ "where d.id=c.deptid and c.id = a.uid and a.sid=#{sid} and a.uid=b.uid group by a.uid")
	List<Attendance> statistics(@Param("sid") int sid);
	
	@Select("select count(*) from sessionCounter where sid=#{sid}")
	int shouldAttend(@Param("sid") int sid);
	
	@Select("select count(*) from sessionAttendance where sid=#{sid} and uid=#{uid}")
	int actualAttend(@Param("sid") int sid, @Param("uid") int uid);
}
