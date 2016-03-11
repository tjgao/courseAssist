package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.courseAssist.model.CourseTpl;
import org.courseAssist.model.Department;
import org.courseAssist.model.University;

public interface MgrMapper {
	@Select("select * from universities")
	List<University> listUni();
	
	@Select("select * from dept where uniid=#{uniid} ")
	List<Department> listDept(@Param("uniid") int uniid);
	
	@Update("update dept set deptname=#{deptname} where id=#{id} ")
	void updateDept(@Param("id") int id, @Param("deptname") String deptname);
	
	@Insert("insert into dept (deptname, uniid) values (#{deptname}, #{uniid}) ")
	void insertDept(@Param("deptname") String deptname, @Param("uniid") int uniid);
	
	@Select("select * from courses where uniid=#{uniid} order by id desc limit #{start}, #{limit}")
	List<CourseTpl> listCourses(@Param("uniid") int uniid, @Param("start") int start, @Param("limit") int limit);
	
	@Select("select count(*) from courses where uniid=#{uniid}")
	int countCourses(@Param("uniid") int uniid);
	
	@Select("select * from courses where uniid=#{uniid} and deptid = #{deptid} order by id desc limit #{limit}, #{start}")
	List<CourseTpl> listCoursesByDept(@Param("uniid") int uniid, @Param("deptId") int deptId, @Param("start") int start, @Param("limit") int limit);

	@Select("select count(*) from courses where uniid=#{uniid} and deptId=#{deptId}")
	int countCoursesByDept(@Param("uniid") int uniid, @Param("deptId") int deptId);
}
