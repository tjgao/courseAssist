package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.SessionMsg;

public interface SessionMsgMapper {
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} order by id desc limit #{start}, #{limit}")
	List<SessionMsg> getReceivedMessages( @Param("receiver") int receiver, 
			@Param("start") int start, @Param("limit") int limit);
	

	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} order by id desc")
	List<SessionMsg> getAllReceivedMessages( @Param("receiver") int receiver);


	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} and a.id > #{id} order by id desc")
	List<SessionMsg> getReceiveNewMessages( @Param("id") int id, @Param("receiver") int receiver);
	

	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} and a.id < #{id} order by id desc limit 0, #{limit}")
	List<SessionMsg> getReceiveOldMessages( @Param("id") int id, @Param("receiver") int receiver, @Param("limit") int limit);

	@Insert("insert into sessionMsg ( receiver, sender, title, content, time) "
			+ "values (#{receiver},#{sender},#{title},#{content},now())")
	void sendMessage(SessionMsg sm);
	
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, a.content, b.realname as senderName, b.headimg as senderHead "
			+ "from sessionMsg as a, user as b where a.sender=b.id and a.id=#{id}")
	SessionMsg readMessage(@Param("id") int id);
}
