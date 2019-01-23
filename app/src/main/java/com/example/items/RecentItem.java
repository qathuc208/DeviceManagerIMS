package com.example.items;

public class RecentItem {

	String imei;
	String type;
	String content;
	String time;

	public String getType() {
		return type;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public RecentItem(String imei, String type, String content, String time) {
		super();
		this.imei = imei;
		this.type = type;
		this.content = content;
		this.time = time;
	}

	
	

}