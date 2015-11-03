package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.SessionMsg;

public interface SessionMsgMapper {
	@Select("select * from sessionMsg where sid=#{sid} and receiver=#{receiver} order by id desc limit #{start}, #{limit}")
	List<SessionMsg> getReceivedMessages(@Param("sid") int sid, @Param("reciver") int receiver, 
			@Param("start") int start, @Param("limit") int limit);
	
	@Insert("insert into sessionMsg (sid, receiver, sender, title, content, time) "
			+ "values (#{sid},#{receiver},#{sender},#{title},#{content},now())")
	void sendMessage(SessionMsg sm);
}
