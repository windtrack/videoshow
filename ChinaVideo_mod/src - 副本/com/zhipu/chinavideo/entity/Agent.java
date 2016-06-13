package com.zhipu.chinavideo.entity;

public class Agent {
	private String id;
	private String nickname;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "Agent [id=" + id + ", nickname=" + nickname + "]";
	}

	public Agent(String id, String nickname) {
		super();
		this.id = id;
		this.nickname = nickname;
	}

	public Agent() {
		super();
	}

}
