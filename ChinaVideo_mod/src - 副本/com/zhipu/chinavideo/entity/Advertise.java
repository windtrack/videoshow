package com.zhipu.chinavideo.entity;

public class Advertise {
	private String img;
	private String act;
	private String room_id;
	private String url;
	public Advertise(String img, String act, String room_id, String url) {
		super();
		this.img = img;
		this.act = act;
		this.room_id = room_id;
		this.url = url;
	}
	public Advertise() {
		super();
	}
	@Override
	public String toString() {
		return "Advertise [img=" + img + ", act=" + act + ", room_id="
				+ room_id + ", url=" + url + "]";
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getRoom_id() {
		return room_id;
	}
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
}
