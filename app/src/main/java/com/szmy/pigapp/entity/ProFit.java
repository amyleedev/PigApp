package com.szmy.pigapp.entity;

import java.io.Serializable;

public class ProFit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userID;//收益人id
	private String account;//收益人帐号	
	private String presentee;//被推荐人帐号
	private String productID;//商品id
	private String productName;//商品名称
	private String number;//商品数量
	private String price;//商品单价
	private String profit;//单个收益
	private String ratio ;//收益比例
	private String finalProfit;//最终总收益
	private String createTime;//收益时间
	private String compName;//公司名称
	private String compID;//公司Id		
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String type;//类型,1为系统 2为商家 3为普通用户	
	private String source ;//数据来源1：被推荐人购买收益）（2：平台购买支付）(3:猪贸通推广注册收益)(4:猪贸通签到收益)
	private String remark ;//备注
	private String productImg ;//商品图片
	private String xdrq;//下单时间
	private String orderID;//订单id
	private String orderStatus;//订单状态
	private String status;//收益状态 y可用  n不可用    默认值为y  只有source为1被推荐人购买提成的的时候才为n, 确认收货后改为y
	private String city ;//地区
	private String sourceName;//数据来源名称

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getXdrq() {
		return xdrq;
	}

	public void setXdrq(String xdrq) {
		this.xdrq = xdrq;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPresentee() {
		return presentee;
	}
	public void setPresentee(String presentee) {
		this.presentee = presentee;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getFinalProfit() {
		return finalProfit;
	}
	public void setFinalProfit(String finalProfit) {
		this.finalProfit = finalProfit;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public String getCompID() {
		return compID;
	}
	public void setCompID(String compID) {
		this.compID = compID;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
