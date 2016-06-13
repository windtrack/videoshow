package com.zhipu.chinavideo.entity;

/**
 * @author: zhongxf
 * @Description: 支付方式的实体类
 * @date: 2016年4月11日
 */
public class PayTypeVo implements Comparable{
	private int num;
	private String name;
	private int type;
	public PayTypeVo(int num,String name,int type){
		this.num = num;
		this.name = name;
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 *@author： zhongxf
	 *@Description: 重写排序方法
	 */
	@Override
	public int compareTo(Object arg0) {
		PayTypeVo vo = (PayTypeVo)arg0;
		return vo.getNum() - num;//倒序排序
	}
	
	
	
}
