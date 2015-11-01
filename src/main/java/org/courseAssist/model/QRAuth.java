package org.courseAssist.model;

import java.util.Date;

public class QRAuth {
	// automatically generated
	private int id;
	// 
	private String code;
	private String token;
	
	private int confirmed;
	// ip for this PC server
	private long ip;
	// default port for this PC server
	private int port;
	// teacher's id
	private int uid;
	// session's id
	private int sid;
	// time
	private Date time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}
	public long getIp() {
		return ip;
	}
	public void setIp(long ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
