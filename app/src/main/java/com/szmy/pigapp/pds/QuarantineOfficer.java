package com.szmy.pigapp.pds;

import java.io.Serializable;

public class QuarantineOfficer implements Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String bjd;//           ： 报检点的id
	private String num;//           ： 数量
	private String area;//          ： 地区
	private String sort;//          :  种类
	private String pjtz;//          ： 平均体重
	private String tel;//           ： 电话
	private String code;//          :  申报编号
	private String huozhu ;//       :  货主
	private String sbdate;//      ： 申报时间
	private String  cl_result ;//    :  处理结果，1 处理通过。0 处理未通过  “ ”或者null 未处理
	private String  cl_reason ;//    ： 处理未通过时的原因
	private String  cl_user  ;//     ： 处理用户名称
	private String  cl_sdate ;//     ： 处理日期  （2015年04月22日）
	private String  cl_date ;//      ： 处理日期  （2015-04-22 15:37:00.0）
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBjd() {
		return bjd;
	}
	public void setBjd(String bjd) {
		this.bjd = bjd;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getPjtz() {
		return pjtz;
	}
	public void setPjtz(String pjtz) {
		this.pjtz = pjtz;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getHuozhu() {
		return huozhu;
	}
	public void setHuozhu(String huozhu) {
		this.huozhu = huozhu;
	}
	
	public String getSbdate() {
		return sbdate;
	}
	public void setSbdate(String sbdate) {
		this.sbdate = sbdate;
	}
	public String getCl_result() {
		return cl_result;
	}
	public void setCl_result(String cl_result) {
		this.cl_result = cl_result;
	}
	public String getCl_reason() {
		return cl_reason;
	}
	public void setCl_reason(String cl_reason) {
		this.cl_reason = cl_reason;
	}
	public String getCl_user() {
		return cl_user;
	}
	public void setCl_user(String cl_user) {
		this.cl_user = cl_user;
	}
	public String getCl_sdate() {
		return cl_sdate;
	}
	public void setCl_sdate(String cl_sdate) {
		this.cl_sdate = cl_sdate;
	}
	public String getCl_date() {
		return cl_date;
	}
	public void setCl_date(String cl_date) {
		this.cl_date = cl_date;
	}
	
}
