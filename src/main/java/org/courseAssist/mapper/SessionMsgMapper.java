package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.mapping.StatementType;
import org.courseAssist.model.MessageContent;
import org.courseAssist.model.SessionMsg;

public interface SessionMsgMapper {
	/*
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} order by id desc limit #{start}, #{limit}")
	*/
	@Select("select a.id,a.sender,a.receiver,c.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionmsg as a, user as b, msgcontent as c where "
			+ "a.sender=b.id and a.mid = c.id and receiver=#{receiver} order by id desc limit #{start},#{limit};")
	List<SessionMsg> getReceivedMessages( @Param("receiver") int receiver, 
			@Param("start") int start, @Param("limit") int limit);
	
	/*
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} order by id desc")
	*/
	@Select("select a.id,a.sender,a.receiver,c.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionmsg as a, user as b, msgcontent as c "
			+ "where a.sender=b.id and c.id = a.mid and receiver=#{receiver} order by id desc")
	List<SessionMsg> getAllReceivedMessages( @Param("receiver") int receiver);


	/*
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} and a.id > #{id} order by id desc")
	*/
	@Select("select a.id,a.sender,a.receiver,c.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionmsg as a, user as b, msgcontent as c "
			+ "where a.sender=b.id and c.id = a.mid and receiver=#{receiver} and a.id > #{id} order by id desc ")
	List<SessionMsg> getReceiveNewMessages( @Param("id") int id, @Param("receiver") int receiver);
	

	/*
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionMsg as a, user as b where "
			+ "a.sender=b.id and receiver=#{receiver} and a.id < #{id} order by id desc limit 0, #{limit}")
	*/
	@Select("select a.id,a.sender,a.receiver,c.title,a.time, b.realname as senderName, "
			+ "b.headimg as senderHead from sessionmsg as a, user as b, msgcontent as c "
			+ "where a.sender=b.id and c.id = a.mid and receiver=#{receiver} and a.id < #{id} order "
			+ "by id desc limit 0, #{limit}")
	List<SessionMsg> getReceiveOldMessages( @Param("id") int id, @Param("receiver") int receiver, @Param("limit") int limit);

	@Insert("insert into sessionmsg ( receiver, sender, mid, time) "
			+ "values (#{receiver},#{sender},#{mid},now())")
	void sendMessage(SessionMsg sm);
	
	@Insert("insert into msgcontent( title, content) values (#{title},#{content})")
	void storeMsg(MessageContent mc);
	
	@Select("select last_insert_id()")
	int lastMsgId();
	
	/*
	@Select("select a.id,a.sender,a.receiver,a.title,a.time, a.content, b.realname as senderName, b.headimg as senderHead "
			+ "from sessionMsg as a, user as b where a.sender=b.id and a.id=#{id}")
	*/
	@Select("select a.id,a.sender,a.receiver,c.title, a.time, c.content, b.realname as "
			+ "senderName, b.headimg as senderHead from sessionmsg as a, user as b, "
			+ "msgcontent as c where a.sender=b.id and c.id = a.mid and a.id=#{id}")
	SessionMsg readMessage(@Param("id") int id);
}
