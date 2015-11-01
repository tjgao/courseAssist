package org.courseAssist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.courseAssist.model.CourseWare;

public interface CourseWareMapper {
	@Select("")
	List<CourseWare> getAllCourseWare(int sid);
}
