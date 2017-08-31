package com.szmy.pigapp.vehicle;

import java.io.Serializable;

public class Vehicle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String price;// 运费
	private String startTime;// 限租开始时间
	private String endTime;// 限租结束时间
	private String type;// 车型
	private String carNum;// 车牌号
	private String capacity;// 容量
	private String companyId;// 公司ID ，如果是个人 发布信息则是空的
	private String address;// 地址
	private String linkman;// 联系人
	private String phone;// 联系方式
	private String icon;// 图标
	private String createTime;// 创建时间
	private String createAccount;// 创建人
	private String province;// 省代码
	private String city;// 市代码
	private String area;// 县代码
	private String remark;
	private String name;
	private String status;
	private String purchaserID;
	private String purchaserName;
	private String purchaserPhone;
	private String finalPrice;
	private double x;
	private double y;
	private String carRadius;//服务半径
	private String pigType;//生猪类型
	

	// private String pictureList

	

	public String getFinalPrice() {
		return finalPrice;
	}

	public String getCarRadius() {
		return carRadius;
	}

	public void setCarRadius(String carRadius) {
		this.carRadius = carRadius;
	}

	public String getPigType() {
		return pigType;
	}

	public void setPigType(String pigType) {
		this.pigType = pigType;
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

	public void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getPurchaserID() {
		return purchaserID;
	}

	public void setPurchaserID(String purchaserID) {
		this.purchaserID = purchaserID;
	}

	public String getPurchaserName() {
		return purchaserName;
	}

	public void setPurchaserName(String purchaserName) {
		this.purchaserName = purchaserName;
	}

	public String getPurchaserPhone() {
		return purchaserPhone;
	}

	public void setPurchaserPhone(String purchaserPhone) {
		this.purchaserPhone = purchaserPhone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateAccount() {
		return createAccount;
	}

	public void setCreateAccount(String createAccount) {
		this.createAccount = createAccount;
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

}
