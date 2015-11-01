package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.Information;

public interface InformationMapper {
	@Select("(select id, title, content, time, '全校' as deptName from information"
			+ " where uniid = #{uniid} and deptid=0) union (select a.id as id, a.title as"
			+ " title, a.content as content, a.time as time, b.deptname as deptName"
			+ " from information as a, dept as b where a.deptid=b.id and a.deptid=#{deptid}"
			+ " and a.type=#{type}) order by id desc limit #{start},#{limit}")
	List<Information> getInformation(int uniid, int deptid, int type, int start, int limit);
}
