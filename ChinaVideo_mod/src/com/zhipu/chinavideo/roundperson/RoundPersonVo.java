package com.zhipu.chinavideo.roundperson;

/**
 * @author: zhongxf
 * @Description: 周边大人的实体类
 * @date: 2016年4月13日
 */
public class RoundPersonVo {
	private String imgUrl;//头像的地址
	private String id;//编号
	private String name;//姓名
	private String type;//类型， p:代表玩家   z:代表主播
	private int dis;//距离
	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}
	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the dis
	 */
	public int getDis() {
		return dis;
	}
	/**
	 * @param dis the dis to set
	 */
	public void setDis(int dis) {
		this.dis = dis;
	}
	
}
