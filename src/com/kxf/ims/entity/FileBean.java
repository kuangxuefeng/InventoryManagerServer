package com.kxf.ims.entity;


public class FileBean {
	private int id;
	private String name;
	private String path;
	private int type; //1已存在，2需要下载，3需要上送
	private String fileSrc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFileSrc() {
		return fileSrc;
	}
	public void setFileSrc(String fileSrc) {
		this.fileSrc = fileSrc;
	}
	@Override
	public String toString() {
		return "FileBean [id=" + id + ", name=" + name + ", path=" + path
				+ ", type=" + type + ", fileSrc=" + fileSrc + "]";
	}
}
