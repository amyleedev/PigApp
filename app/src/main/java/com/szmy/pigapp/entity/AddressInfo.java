package com.szmy.pigapp.entity;

import java.io.Serializable;
/**
 * 收货地址
 * @author qing
 *
 */
public class AddressInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  id ;
	private String  account;//会员帐号
	private String name;//      | varchar(45)  |收货人姓名
	private String  address;//   | varchar(245) |详细地址
	private String  zip;//       | varchar(6)   |邮编
	private String phone;//     | varchar(25)  |电话
	private String mobile;//    | varchar(25)  |手机
	private String isdefault; //| varchar(2)   |是否默认地址
	private String province ;// | varchar(15)  |省 代码
	private String city    ;  //| varchar(15)  |市 代码
	private String area    ; // | varchar(15)  |区/县 代码
	private String pcadetail ;//| varchar(75)  | 省市县中文拼接汉字
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
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
	public String getPcadetail() {
		return pcadetail;
	}
	public void setPcadetail(String pcadetail) {
		this.pcadetail = pcadetail;
	}
	

}
