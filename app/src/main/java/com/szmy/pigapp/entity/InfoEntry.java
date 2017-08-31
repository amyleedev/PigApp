package com.szmy.pigapp.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class InfoEntry implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2512398558763287653L;
	private String address;
	private String coverPicture;
	private String createTime;
	private String id;
	private String marketingTime;
	private String number;
	private String orderStatus;
	private String price;
	private String remark;
	private String weight;
	private String title;
	private String userID;
	private String userName;
	private String phone;
	private String finalPrice;
	private String province;// 省
	private String city;// 市
	private String area;// 县
	private String orderType;// 买卖
	private String pigType;
	private String purchaserID;
	private String purchaserPhone;
	private String purchaserName;
	private String color;
	private double x;
	private double y;
	private String compName;
	private String sendType;// 配送方式
	// private String gbd;//过磅单
	// private String btrd;//白条肉单
	private String paymentDays;// 延期时间
	private String purchaserType;
	private String checkStatus;// 1未审核2审核通过3审核不通过
	private String reason;// 审核未通过原因
	private String isFriend;// 用户的关注状态
	private String userType;// 发布者角色
	private String ygj;//预估价
	private String xdrxxf;//下单人信息费
	private String fbrxxf;//发布人信息费
	private String payType;//付款方式
	private String iszd;//置顶标识
	private String payDeadline;//失效时间付款实践性限制
	private String diff;//付款剩余时间
	private String startPrice;//生猪单价最大值
	private String endPrice;//生猪单价最小值

	public String getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}

	public String getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(String endPrice) {
		this.endPrice = endPrice;
	}

	public String getDiff() {
		return diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public String getIszd() {
		return iszd;
	}

	public void setIszd(String iszd) {
		this.iszd = iszd;
	}

	public String getPayDeadline() {
		return payDeadline;
	}

	public void setPayDeadline(String payDeadline) {
		this.payDeadline = payDeadline;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getXdrxxf() {
		return xdrxxf;
	}

	public void setXdrxxf(String xdrxxf) {
		this.xdrxxf = xdrxxf;
	}

	public String getFbrxxf() {
		return fbrxxf;
	}

	public void setFbrxxf(String fbrxxf) {
		this.fbrxxf = fbrxxf;
	}

	public String getYgj() {
		return ygj;
	}

	public void setYgj(String ygj) {
		this.ygj = ygj;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPurchaserType() {
		return purchaserType;
	}

	public void setPurchaserType(String purchaserType) {
		this.purchaserType = purchaserType;
	}

	public String getPaymentDays() {
		return paymentDays;
	}

	public void setPaymentDays(String paymentDays) {
		this.paymentDays = paymentDays;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getPurchaserName() {
		return purchaserName;
	}

	public void setPurchaserName(String purchaserName) {
		this.purchaserName = purchaserName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getPigType() {
		return pigType;
	}

	public void setPigType(String pigType) {
		this.pigType = pigType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}

	private ArrayList<String> picturelist = new ArrayList<String>();

	public ArrayList<String> getPicturelist() {
		return picturelist;
	}

	public void setPicturelist(ArrayList<String> picturelist) {
		this.picturelist = picturelist;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCoverPicture() {
		return coverPicture;
	}

	public void setCoverPicture(String coverPicture) {
		this.coverPicture = coverPicture;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMarketingTime() {
		return marketingTime;
	}

	public void setMarketingTime(String marketingTime) {
		this.marketingTime = marketingTime;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPurchaserID() {
		return purchaserID;
	}

	public void setPurchaserID(String purchaserID) {
		this.purchaserID = purchaserID;
	}

	public String getPurchaserPhone() {
		return purchaserPhone;
	}

	public void setPurchaserPhone(String purchaserPhone) {
		this.purchaserPhone = purchaserPhone;
	}

	// public String getGbd() {
	// return gbd;
	// }
	//
	// public void setGbd(String gbd) {
	// this.gbd = gbd;
	// }
	//
	// public String getBtrd() {
	// return btrd;
	// }
	//
	// public void setBtrd(String btrd) {
	// this.btrd = btrd;
	// }

}
