package com.zhipu.chinavideo.entity;

public class Tasks {
	private String id;
	private String icon;
	private String schedule;
	private String schedule_limited;
	private String status;
	private String name;
	private String task_id;
	private String award;
	private String prizes;
	private String sort_index;
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
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getSchedule_limited() {
		return schedule_limited;
	}
	public void setSchedule_limited(String schedule_limited) {
		this.schedule_limited = schedule_limited;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getPrizes() {
		return prizes;
	}
	public void setPrizes(String prizes) {
		this.prizes = prizes;
	}
	public String getSort_index() {
		return sort_index;
	}
	public void setSort_index(String sort_index) {
		this.sort_index = sort_index;
	}
	@Override
	public String toString() {
		return "Tasks [id=" + id + ", icon=" + icon + ", schedule=" + schedule
				+ ", schedule_limited=" + schedule_limited + ", status="
				+ status + ", name=" + name + ", task_id=" + task_id
				+ ", award=" + award + ", prizes=" + prizes + ", sort_index="
				+ sort_index + "]";
	}
	public Tasks(String id, String icon, String schedule,
			String schedule_limited, String status, String name,
			String task_id, String award, String prizes, String sort_index) {
		super();
		this.id = id;
		this.icon = icon;
		this.schedule = schedule;
		this.schedule_limited = schedule_limited;
		this.status = status;
		this.name = name;
		this.task_id = task_id;
		this.award = award;
		this.prizes = prizes;
		this.sort_index = sort_index;
	}
	public Tasks() {
		super();
	}

	
}
