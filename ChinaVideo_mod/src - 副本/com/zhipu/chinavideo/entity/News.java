package com.zhipu.chinavideo.entity;

public class News {
	private String icon;
	private String url;
	private String description;
	private String title;
	private String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public News(String icon, String url, String description, String title) {
		super();
		this.icon = icon;
		this.url = url;
		this.description = description;
		this.title = title;
	}

	public News() {
		super();
	}

	@Override
	public String toString() {
		return "News [icon=" + icon + ", url=" + url + ", description="
				+ description + ", title=" + title + "]";
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
