package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.courseAssist.model.User;
import org.courseAssist.model.UserExt;

public interface UserMapper {
	@Select("select * from user where pwd = #{pwd} and name=#{name}")
	User getUserByNamePwd(@Param("name") String name, @Param("pwd") String pwd);
	
	@Select("select * from user where pwd = #{pwd} and id=#{id}")
	User getUserByIdPwd(@Param("id") int id, @Param("pwd") String pwd);
	
	@Select("select * from user where id=#{id}")
	User getUserById(@Param("id") int id);
	
	@Select("select a.id as id, a.pid as pid, a.nickname as nickname, a.sex as sex,"
			+ "a.mobile as mobile, a.email as email, a.pwd as pwd, a.name as name, a.realname as "
			+ "realname, a.headimg as headimg, b.name as uniname, c.deptname as deptname "
			+ "from user as a, universities as b, dept as c where a.name=#{name} and "
			+ "a.deptid = c.id and c.uniid = b.id;")
	User getUserByName(@Param("name") String name);
	
	@Update("update user set pwd=#{pwd} where id=#{id}")
	void chgPasswordById(@Param("id") int id, @Param("pwd") String pwd);
	
	@Update("update user set pwd=#{pwd} where name=#{name}")
	void chgPasswordByName(@Param("name") String name, String pwd);
	
	@Select("select b.* from sessionUser as a, user as b where a.sid=#{sid} and a.uid=#{uid} and a.uid=b.id")
	User getUserBySidUid(@Param("sid") int sid, @Param("uid") int uid);	
	
	@Select("select * from sessionUser as a, user as b where a.sid=#{sid} and a.uid=b.id")
	List<User> getUsersBySid(@Param("sid") int sid);
	
	@Select("select b.id, b.sex, b.realname, b.email, b.mobile, b.sex, b.pid, a.privilege, "
			+ "b.headimg from sessionUser as a, user as b where a.sid=#{sid} and a.uid=b.id")
	List<UserExt> getUserExtBySid(@Param("sid") int sid);
}
