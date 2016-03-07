package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.courseAssist.model.Information;

public interface InformationMapper {
	@Select("(select a.id, a.uniid, a.title, a.time, a.deptid, b.name as uniName, '全校' "
			+ "as deptName from information a, universities as b where deptid=0 and b.id = a.uniid "
			+ "and a.uniid=#{uniid}) union (select a.id as id, a.uniid as uniid, a.title as title, "
			+ " a.time as time, a.deptid as deptid, c.name as uniName, b.deptname as "
			+ "deptName from information as a, dept as b, universities as c where a.deptid=b.id "
			+ "and a.deptid=#{deptid} and c.id = a.uniid and a.uniid=#{uniid}) order by id desc "
			+ "limit #{start}, #{limit}")
	List<Information> getInformation(@Param("uniid") int uniid, @Param("deptid") int deptid,  
			@Param("start") int start, @Param("limit") int limit);
	
	@Select("(select a.id, a.uniid, a.title, a.time, a.deptid, b.name as uniName, '全校' "
			+ "as deptName from information a, universities as b where deptid=0 and b.id = a.uniid "
			+ "and a.uniid=#{uniid}) union (select a.id as id, a.uniid as uniid, a.title as title, "
			+ "a.time as time, a.deptid as deptid, c.name as uniName, b.deptname as "
			+ "deptName from information as a, dept as b, universities as c where a.deptid=b.id "
			+ "and a.deptid!=0 and c.id = a.uniid and a.uniid=#{uniid}) order by id desc limit #{start}, #{limit}")
	List<Information> getInformation2(@Param("uniid") int uniid, @Param("start") int start, @Param("limit") int limit);

	@Select("(select a.id, a.uniid, a.title, a.time, a.deptid, b.name as uniName, '全校' as "
			+ "deptName from information a, universities as b where deptid=0 and b.id = a.uniid) "
			+ "union (select a.id as id, a.uniid as uniid, a.title as title, "
			+ "a.time as time, a.deptid as deptid, c.name as uniName, b.deptname as deptName from "
			+ "information as a, dept as b, universities as c where a.deptid=b.id and a.deptid!=0 "
			+ "and c.id = a.uniid) order by id desc limit #{start}, #{limit}")
	List<Information> getAllInformation(@Param("start") int start, @Param("limit") int limit);
	
	@Select("select (select count(*) from information where uniid=#{uniid} and deptid=0) + "
			+ "(select count(*) from information where uniid=#{uniid} and deptid=#{deptid})")
	int getInformationCount(@Param("uniid") int uniid, @Param("deptid") int deptid );
	
	@Select("select count(*) from information where uniid=#{uniid}")
	int getInformationCount2(@Param("uniid") int uniid);
	
	@Insert("insert into information (title, content, deptid, uniid, time ) "
			+ "values (#{title},#{content},#{deptid},#{uniid}, now() )")
	void insertInfo(Information i);
	
	@Update("update information set title=#{title}, content=#{content}, deptid=#{deptid}, "
			+ "uniid=#{uniid} where id=#{id}")
	void updateInfo(Information i);
	
	@Delete("delete from information where id=#{id}")
	void deleteInfo(@Param("id") int id);
	
	@Select("select a.id, a.title, a.content, a.time, a.deptid, b.name as uniName, "
			+ "(case a.deptid when 0 then '全校' else (select deptname from dept where "
			+ "id=a.deptid) end) as deptName from information as a, universities as"
			+ " b where a.uniid=b.id and a.id=#{iid}")
	Information getinfo(@Param("iid") int iid);
}
