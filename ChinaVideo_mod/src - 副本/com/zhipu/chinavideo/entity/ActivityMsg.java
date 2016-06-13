package com.zhipu.chinavideo.entity;

/**
 * @author lushouzhi
 *
 */
public class ActivityMsg {
	private int tid;
	private String msg;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
//	@Override
//	public String toString() {
//		return "ActivityMsg [tid=" + tid + ", msg=" + msg + ", getTid()="
//				+ getTid() + ", getMsg()=" + getMsg() + ", getClass()="
//				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
//				+ super.toString() + "]";
//	}
//	
	@Override
	public String toString() {
		return "ActivityMsg [tid=" + tid + ", msg=" + msg + "]";
	}
	
}
