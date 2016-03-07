package org.courseAssist.model;

public class User {
	private int id;
	private String pid;
	private String name;
	private String nickname;
	private String realname;
	private int sex;
	private String mobile;
	private String email;
	private int deptid;
	private int priv;
	private String pwd;
	private String headimg;
	private String uniname;
	private String deptname;
	private int uniid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getDeptid() {
		return deptid;
	}
	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}

	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getHeadimg() {
		return headimg;
	}
	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}
	public String getUniname() {
		return uniname;
	}
	public void setUniname(String uniname) {
		this.uniname = uniname;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public int getPriv() {
		return priv;
	}
	public void setPriv(int priv) {
		this.priv = priv;
	}
	public int getUniid() {
		return uniid;
	}
	public void setUniid(int uniid) {
		this.uniid = uniid;
	}
}
