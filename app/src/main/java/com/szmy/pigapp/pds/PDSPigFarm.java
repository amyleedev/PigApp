package com.szmy.pigapp.pds;

import java.io.Serializable;
/**
 * 检疫申报
 * @author qing
 *
 */
public class PDSPigFarm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;//            ： id
    private String bjd;//           ： 报检点的id
    private String num;//           ： 数量
    private String area;//          ： 地区
    private String sort;//          :  种类
    private String pjtz;//          ： 平均体重
    private String tel;//           ： 电话
    private String code;//          :  申报编号
    private String huozhu;//        :  货主
    private String signdate;//      ： 申报时间 
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
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	
    
	

}
