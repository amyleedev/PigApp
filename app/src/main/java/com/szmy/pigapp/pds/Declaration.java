package com.szmy.pigapp.pds;

import java.io.Serializable;

public class Declaration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598295482378644896L;
	private String  AREATYPE ;// 40 ,地点类别 0 规模化养殖场 1 散养 2 屠宰场
	private String  ARRIVALLOCAL ;// 山西省,晋中市,太谷县,,,孙耀强 ,到达地点
	private String  ARRIVALTIME ;//  ,到达时间
	private String  CERTID ;// 5d4019aa5225d1410152578711b148bc ,证明编号
	private String  CODE ;// A2016519734 , 编号
	private String  CONSIGNEE ;// 刘军广 ,货主
	private String  DECLDATE ;// 2016-01-19 09;//34;//58.0 , 受理时间
	private String  ID ;// 5d4019aa5225d1410152578547f34884 ,
	private String  INSPDATETIME ;// 2016-01-19 09;//34;//58.0 , 申报时间
	private String  INSPECTOR ;//  ,
	private String  ISOUT ;// 1 ,  区分省内证 省外证
	private String  LEAVELOCAL ;// 河北省,石家庄市,长安区,,,高营镇西古城村 , 启运地点
	private String  LEAVELTIME ;// 2016-01-19 00;//00;//00.0 , 启运时间
	private String  NUM ;// 5000 ,
	private String  OPERATOR ;// 王春刚 , 经办人
	private String  QUARDATE ;// 2016-01-19 00;//00;//00.0 , 检疫时间
	private String  SIGN ;// 0 , 动物类型 0 是动物 1 是动物产品 
	private String  SORT ;// 雏鸡 ,
	private String  STATUS ;// 5 ,//状态(1;//未受理 2：已受理 3：不受理 4;//删除) 
	private String  TELEPHONE ;// 13933000048 ,
	private String  TQUARLOCAL ;// 河北省石家庄市高营镇西古城村 , 检疫地点
	private String  UNIT ;// 只  单位
	public String getAREATYPE() {
		return AREATYPE;
	}
	public void setAREATYPE(String aREATYPE) {
		AREATYPE = aREATYPE;
	}
	public String getARRIVALLOCAL() {
		return ARRIVALLOCAL;
	}
	public void setARRIVALLOCAL(String aRRIVALLOCAL) {
		ARRIVALLOCAL = aRRIVALLOCAL;
	}
	public String getARRIVALTIME() {
		return ARRIVALTIME;
	}
	public void setARRIVALTIME(String aRRIVALTIME) {
		ARRIVALTIME = aRRIVALTIME;
	}
	public String getCERTID() {
		return CERTID;
	}
	public void setCERTID(String cERTID) {
		CERTID = cERTID;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getCONSIGNEE() {
		return CONSIGNEE;
	}
	public void setCONSIGNEE(String cONSIGNEE) {
		CONSIGNEE = cONSIGNEE;
	}
	public String getDECLDATE() {
		return DECLDATE;
	}
	public void setDECLDATE(String dECLDATE) {
		DECLDATE = dECLDATE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getINSPDATETIME() {
		return INSPDATETIME;
	}
	public void setINSPDATETIME(String iNSPDATETIME) {
		INSPDATETIME = iNSPDATETIME;
	}
	public String getINSPECTOR() {
		return INSPECTOR;
	}
	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}
	public String getISOUT() {
		return ISOUT;
	}
	public void setISOUT(String iSOUT) {
		ISOUT = iSOUT;
	}
	public String getLEAVELOCAL() {
		return LEAVELOCAL;
	}
	public void setLEAVELOCAL(String lEAVELOCAL) {
		LEAVELOCAL = lEAVELOCAL;
	}
	public String getLEAVELTIME() {
		return LEAVELTIME;
	}
	public void setLEAVELTIME(String lEAVELTIME) {
		LEAVELTIME = lEAVELTIME;
	}
	public String getNUM() {
		return NUM;
	}
	public void setNUM(String nUM) {
		NUM = nUM;
	}
	public String getOPERATOR() {
		return OPERATOR;
	}
	public void setOPERATOR(String oPERATOR) {
		OPERATOR = oPERATOR;
	}
	public String getQUARDATE() {
		return QUARDATE;
	}
	public void setQUARDATE(String qUARDATE) {
		QUARDATE = qUARDATE;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	public String getSORT() {
		return SORT;
	}
	public void setSORT(String sORT) {
		SORT = sORT;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getTELEPHONE() {
		return TELEPHONE;
	}
	public void setTELEPHONE(String tELEPHONE) {
		TELEPHONE = tELEPHONE;
	}
	public String getTQUARLOCAL() {
		return TQUARLOCAL;
	}
	public void setTQUARLOCAL(String tQUARLOCAL) {
		TQUARLOCAL = tQUARLOCAL;
	}
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	
	
}
