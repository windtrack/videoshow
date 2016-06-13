package com.zhipu.chinavideo.entity;

public class StarWeek {
	private String name;
	private String icon;

	public StarWeek() {
		super();
	}

	public StarWeek(String name, String icon) {
		super();
		this.name = name;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "StarWeek [name=" + name + ", icon=" + icon + "]";
	}

}
