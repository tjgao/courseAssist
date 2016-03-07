package org.courseAssist.model;

import java.util.Date;

public class Information {
	private int id;
	private int deptid;
	private int uniid;
	private String title;
	private String content;
	private Date time;
	private String uniName;
	private String deptName;
	private int type;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDeptid() {
		return deptid;
	}
	public void setDeptId(int deptId) {
		this.deptid = deptId;
	}
	public int getUniid() {
		return uniid;
	}
	public void setUniid(int uniid) {
		this.uniid = uniid;
	}
	public String getUniName() {
		return uniName;
	}
	public void setUniName(String uniName) {
		this.uniName = uniName;
	}
}
