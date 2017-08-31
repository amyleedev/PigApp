package com.szmy.pigapp.entity;

import java.io.Serializable;

public class ZhuChang implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	 private String province;//省
	 private String city;//市
	 private String area;//县
	 private String type;//猪场类型
	 private String name;//猪场名称
	 private String amount;//存栏数量
	 private String sales;//年出栏数量
	 private String pigType;//生猪类型
	 private String username;//联系人
	 private String address;//详细地址
	 private String phone;//手机号
	 private String email;//邮箱
	 private String nyBank;//农行卡号
	 private String status;//状态:1为申请,2为通过,3不通过
	 private String userID;//用户id
	 private String createTime;//申请时间
	 private String reason;//不通过原因
	 private String x;
	 private String y;
	 private String isSmrzBank;
	 private String coverPicture;//封面图片
	 private String distance;
	 
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getCoverPicture() {
		return coverPicture;
	}
	public void setCoverPicture(String coverPicture) {
		this.coverPicture = coverPicture;
	}
	public String getIsSmrzBank() {
		return isSmrzBank;
	}
	public void setIsSmrzBank(String isSmrzBank) {
		this.isSmrzBank = isSmrzBank;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getPigType() {
		return pigType;
	}
	public void setPigType(String pigType) {
		this.pigType = pigType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNyBank() {
		return nyBank;
	}
	public void setNyBank(String nyBank) {
		this.nyBank = nyBank;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	 
	 
	 
	 

}
