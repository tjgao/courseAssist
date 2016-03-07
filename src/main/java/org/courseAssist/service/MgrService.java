package org.courseAssist.service;

import java.util.List;

import org.courseAssist.mapper.MgrMapper;
import org.courseAssist.model.CourseTpl;
import org.courseAssist.model.Department;
import org.courseAssist.model.University;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MgrService {
	@Autowired
	MgrMapper mm;
	
	public List<University> listUni() { return mm.listUni(); }
	
	public List<Department> listDept(int uniid) { return mm.listDept(uniid); }
	
	public List<CourseTpl> listCourses(int uniid, int start, int limit) {
		return mm.listCourses(uniid, start, limit);
	}
	
	public List<CourseTpl> listCoursesByDept(int uniid, int deptId, int start, int limit ) {
		return mm.listCoursesByDept(uniid, deptId, start, limit);
	}
	
	public int countCourses(int uniid) {
		return mm.countCourses(uniid);
	}
	
	public int countCoursesByDept(int uniid, int deptId) {
		return mm.countCoursesByDept(uniid, deptId);
	}
}
