package com.szmy.pigapp.entity;

import java.io.Serializable;

/**
 * 收货地址
 * @author qing
 *
 */
public class StatisticsAdmin implements Serializable{
	private static final long serialVersionUID = 1L;
	private String userid;//用户id
	private String compName;//企业名称
	private String allPrice;//成交总价
	private String allNumber;//出栏量
	private String allCount;//发布条数

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAllNumber() {
		return allNumber;
	}

	public void setAllNumber(String allNumber) {
		this.allNumber = allNumber;
	}

	public String getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(String allPrice) {
		this.allPrice = allPrice;
	}

	public String getAllCount() {
		return allCount;
	}

	public void setAllCount(String allCount) {
		this.allCount = allCount;
	}
}
