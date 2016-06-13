package com.zhipu.chinavideo.entity;

public class Fans {
	private String id;
	private String icon;
	private String num;
	private String received_level;
	private String nickname;
	private String cost_level;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getReceived_level() {
		return received_level;
	}

	public void setReceived_level(String received_level) {
		this.received_level = received_level;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCost_level() {
		return cost_level;
	}

	public void setCost_level(String cost_level) {
		this.cost_level = cost_level;
	}

	@Override
	public String toString() {
		return "Fans [id=" + id + ", icon=" + icon + ", num=" + num
				+ ", received_level=" + received_level + ", nickname="
				+ nickname + ", cost_level=" + cost_level + "]";
	}

}
