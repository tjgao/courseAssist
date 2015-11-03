package org.courseAssist.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.courseAssist.model.User;

public interface UserMapper {
	@Select("select * from user where pwd = #{pwd} and name=#{name}")
	User getUserByNamePwd(@Param("name") String name, @Param("pwd") String pwd);
	
	@Select("select * from user where pwd = #{pwd} and id=#{id}")
	User getUserByIdPwd(@Param("id") int id, @Param("pwd") String pwd);
	
	@Select("select * from user where id=#{id}")
	User getUserById(@Param("id") int id);
	
	@Select("select * from user where name=#{name}")
	User getUserByName(@Param("name") String name);
	
	@Update("update user set pwd=#{pwd} where id=#{id}")
	void chgPasswordById(@Param("id") int id, @Param("pwd") String pwd);
	
	@Update("update user set pwd=#{pwd} where name=#{name}")
	void chgPasswordByName(@Param("name") String name, String pwd);
	
	@Select("select b.* from sessionUser as a, user as b where a.sid=#{sid} and a.uid=#{uid} and a.uid=b.id")
	User getUserBySidUid(@Param("sid") int sid, @Param("uid") int uid);	
}
