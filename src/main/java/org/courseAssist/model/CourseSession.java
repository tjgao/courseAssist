package org.courseAssist.model;

import java.util.Date;

public class CourseSession {
	private int sid;
	private String name;
	private Date startDate;
	private Date endDate;
	private String deptName;
	private String uniName;
	private int uid;
	private String lecturer;
	private String lecturerHeader;
	private char tag;
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {													
		this.startDate = startDate;																	
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getUniName() {
		return uniName;
	}
	public void setUniName(String uniName) {
		this.uniName = uniName;
	}
	public String getLecturer() {
		return lecturer;
	}
	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}
	public char getTag() {
		return tag;
	}
	public void setTag(char tag) {
		this.tag = tag;
	}
	public String getLecturerHeader() {
		return lecturerHeader;
	}
	public void setLecturerHeader(String lecturerHeader) {
		this.lecturerHeader = lecturerHeader;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
}
