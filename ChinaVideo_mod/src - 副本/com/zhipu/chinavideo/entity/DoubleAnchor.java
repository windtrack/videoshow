package com.zhipu.chinavideo.entity;

public class DoubleAnchor {
	private String title;
	private String num;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public DoubleAnchor(String title, String num) {
		super();
		this.title = title;
		this.num = num;
	}

	public DoubleAnchor() {
		super();
	}

	@Override
	public String toString() {
		return "DoubleAnchor [title=" + title + ", num=" + num + "]";
	}

}
