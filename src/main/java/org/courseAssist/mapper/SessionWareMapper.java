package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.SessionWare;

public interface SessionWareMapper {
	@Select("select * from sessionWare where sid=#{sid}")
	List<SessionWare> getAllSessionWare(@Param("sid") int sid);
	
	@Select("select * from sessionWare where id=#{id}")
	SessionWare getSessionWare(@Param("id") int id);
	
	@Insert("insert into sessionWare (name, description, filename, sid, uid) "
			+ "values (#{name},#{description},#{filename},#{sid},#{uid})")
	void uploadSessionWare(SessionWare s);
	
	@Delete("delete from sessionWare where id=#{id}")
	void deleteSessionWare(@Param("id") int id);
	
	@Select("select * from sessionWare where id=#{id}")
	SessionWare getcw(@Param("id") int id);
}
