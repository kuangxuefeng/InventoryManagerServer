package com.kxf.ims.entity;

import com.kxf.mysqlmanage.annotations.DBAnnotation;

public class Commodity {
	@DBAnnotation(isKey = true)
	private int id;
	private int userId;
	private String name;
	private String qcode;
	private String ymd;//yyyyMMdd
	private String hmsS;//HHmmssSSS
	private int state = 0;//0已入库，1是以出库
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQcode() {
		return qcode;
	}
	public void setQcode(String qcode) {
		this.qcode = qcode;
	}
	public String getYmd() {
		return ymd;
	}
	public void setYmd(String ymd) {
		this.ymd = ymd;
	}
	public String getHmsS() {
		return hmsS;
	}
	public void setHmsS(String hmsS) {
		this.hmsS = hmsS;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Commodity [id=" + id + ", userId=" + userId + ", name=" + name
				+ ", qcode=" + qcode + ", ymd=" + ymd + ", hmsS=" + hmsS
				+ ", state=" + state + "]";
	}
}
