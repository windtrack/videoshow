package com.zhipu.chinavideo.entity;

public class Rank {
	private String icon;
	private String nickname;
	private String user_id;
	private String received_level;
	private String cost_level;
	private String fans_num;
	private String poster_url2;
	private String giftnum;
	public Rank(String icon, String nickname, String user_id,
			String received_level, String cost_level, String fans_num,
			String poster_url2, String giftnum) {
		super();
		this.icon = icon;
		this.nickname = nickname;
		this.user_id = user_id;
		this.received_level = received_level;
		this.cost_level = cost_level;
		this.fans_num = fans_num;
		this.poster_url2 = poster_url2;
		this.giftnum = giftnum;
	}
	public Rank() {
		super();
	}
	@Override
	public String toString() {
		return "Rank [icon=" + icon + ", nickname=" + nickname + ", user_id="
				+ user_id + ", received_level=" + received_level
				+ ", cost_level=" + cost_level + ", fans_num=" + fans_num
				+ ", poster_url2=" + poster_url2 + ", giftnum=" + giftnum + "]";
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getReceived_level() {
		return received_level;
	}
	public void setReceived_level(String received_level) {
		this.received_level = received_level;
	}
	public String getCost_level() {
		return cost_level;
	}
	public void setCost_level(String cost_level) {
		this.cost_level = cost_level;
	}
	public String getFans_num() {
		return fans_num;
	}
	public void setFans_num(String fans_num) {
		this.fans_num = fans_num;
	}
	public String getPoster_url2() {
		return poster_url2;
	}
	public void setPoster_url2(String poster_url2) {
		this.poster_url2 = poster_url2;
	}
	public String getGiftnum() {
		return giftnum;
	}
	public void setGiftnum(String giftnum) {
		this.giftnum = giftnum;
	}
	
	

}
