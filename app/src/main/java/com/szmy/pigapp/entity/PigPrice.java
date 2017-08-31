package com.szmy.pigapp.entity;

import java.io.Serializable;

public class PigPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String createTime;//创建时间
	private String price;//价格
	private String province;//省
	private String city;//市
	private String area;//区
	private String source;//来源(猪贸通,神州牧易平台)
	private String userName;//用户名
	private String type;//类型(生猪,玉米,豆粕,仔猪)
	private String productType;//商品类型(内三元,外三元,土杂猪)(15%水分玉米)(43%蛋白豆粕)(15公斤内仔猪,20公斤内仔猪)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	
}
