package com.szmy.pigapp.entity;

import java.io.Serializable;

public class Ecaluate implements Serializable {

	private String content;// 评论内容
	private String star;// 评价星级
	private String createTime;// 评论时间
	private String orderType;// 订单类型(出售:1,收购:2)(物流:3 )(平台:4)
	private String orderID;// 订单id
	private String productID;// 商品Id
	private String productName;// 商品名称
	private String userName;// 用户名
	private String bpjrType;// 被评价人用户类型(买方:1)(卖方:2)
	private String parentID;// 父id(一级为0)
	private String bpjr;// 被评价人

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBpjrType() {
		return bpjrType;
	}

	public void setBpjrType(String bpjrType) {
		this.bpjrType = bpjrType;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getBpjr() {
		return bpjr;
	}

	public void setBpjr(String bpjr) {
		this.bpjr = bpjr;
	}

}
