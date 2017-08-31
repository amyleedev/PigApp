package com.szmy.pigapp.tixian;

import java.io.Serializable;
/**
 * 提现
 * @author qing
 *
 */
public class Withdrawals implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userId;//用户ID
	private String userName;//用户账户名	
	private String bankNum;//用户银行账户
	private String bankName;//银行卡类型名称//例 农业银行
	private String name;//银行账户名
	private String type;//账户类型   银行卡 支付宝  微信支付
	private String sfzh;//身份证号
	private String sfzZm;//身份证正面
	private String sfzFm; //身份证反面
	private String userCard;//签约客户号

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getUserCard() {
		return userCard;
	}
	public void setUserCard(String userCard) {
		this.userCard = userCard;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBankNum() {
		return bankNum;
	}
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getSfzZm() {
		return sfzZm;
	}
	public void setSfzZm(String sfzZm) {
		this.sfzZm = sfzZm;
	}
	public String getSfzFm() {
		return sfzFm;
	}
	public void setSfzFm(String sfzFm) {
		this.sfzFm = sfzFm;
	}
	
	
}
