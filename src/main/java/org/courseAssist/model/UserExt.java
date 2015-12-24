package org.courseAssist.model;

public class UserExt extends User {
	private int shouldAttend;
	private int actualAttend;
	private int privilege;
	public int getShouldAttend() {
		return shouldAttend;
	}
	public void setShouldAttend(int shouldAttend) {
		this.shouldAttend = shouldAttend;
	}
	public int getActualAttend() {
		return actualAttend;
	}
	public void setActualAttend(int actualAttend) {
		this.actualAttend = actualAttend;
	}
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
}
