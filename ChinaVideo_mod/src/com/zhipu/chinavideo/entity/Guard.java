package com.zhipu.chinavideo.entity;

public class Guard {
	private String id;
	private String icon;
	private String level;
	private String end_time;
	private String nickname;
	private String cost_level;
	private String ol;
	private String days_remain;
	private String type;
	private String is_year;

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
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

	public String getOl() {
		return ol;
	}

	public void setOl(String ol) {
		this.ol = ol;
	}

	public String getDays_remain() {
		return days_remain;
	}

	public void setDays_remain(String days_remain) {
		this.days_remain = days_remain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIs_year() {
		return is_year;
	}

	public void setIs_year(String is_year) {
		this.is_year = is_year;
	}

	public Guard(String id, String icon, String level, String end_time,
			String nickname, String cost_level, String ol, String days_remain,
			String type, String is_year) {
		super();
		this.id = id;
		this.icon = icon;
		this.level = level;
		this.end_time = end_time;
		this.nickname = nickname;
		this.cost_level = cost_level;
		this.ol = ol;
		this.days_remain = days_remain;
		this.type = type;
		this.is_year = is_year;
	}

	public Guard() {
		super();
	}

	@Override
	public String toString() {
		return "Guard [id=" + id + ", icon=" + icon + ", level=" + level
				+ ", end_time=" + end_time + ", nickname=" + nickname
				+ ", cost_level=" + cost_level + ", ol=" + ol
				+ ", days_remain=" + days_remain + ", type=" + type
				+ ", is_year=" + is_year + "]";
	}

}
