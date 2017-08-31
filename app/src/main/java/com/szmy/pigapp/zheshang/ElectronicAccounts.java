package com.szmy.pigapp.zheshang;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/21 0021.
 */
public class ElectronicAccounts implements Serializable {
    private String id;
    private String userID; // 用户id
    private String userName; // 用户名
    private String cardStatus; // 开立电子账户 状态 0：未开，1：已开，2：解绑
    private String cardCertNo; // 开立电子账户 证件号
    private String cardType; // 开立电子账户 电子账户类别 1对公，2对私
    private String cardName; // 开立电子账户 电子账户的名称（如果是公司就写公司名，如果是个人就写个人姓名）
    private String cardCertType; // 开立电子账户 证件类型（）
    private String cardMobile; // 开立电子账户 证件关联手机号
    private String cardNo; // 开立电子账户 卡号
    private String cardOpenName; // 银行卡名称
    private String cardBranchNo; // 人民银行联行行号

    private String bandStatus; // 代收协议的状态 0：未开，1：已开，2：解绑
    private String bandNo; // 代收协议 的银行卡号
    private String bandName; // 代收协议 银行卡名称
    private String bandMobile; // 代收协议 手机号
    private String bandIdentityNo; // 代收协议 身份证号码

    public String getCardOpenName() {
        return cardOpenName;
    }

    public void setCardOpenName(String cardOpenName) {
        this.cardOpenName = cardOpenName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardCertNo() {
        return cardCertNo;
    }

    public void setCardCertNo(String cardCertNo) {
        this.cardCertNo = cardCertNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardCertType() {
        return cardCertType;
    }

    public void setCardCertType(String cardCertType) {
        this.cardCertType = cardCertType;
    }

    public String getCardMobile() {
        return cardMobile;
    }

    public void setCardMobile(String cardMobile) {
        this.cardMobile = cardMobile;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardBranchNo() {
        return cardBranchNo;
    }

    public void setCardBranchNo(String cardBranchNo) {
        this.cardBranchNo = cardBranchNo;
    }

    public String getBandStatus() {
        return bandStatus;
    }

    public void setBandStatus(String bandStatus) {
        this.bandStatus = bandStatus;
    }

    public String getBandNo() {
        return bandNo;
    }

    public void setBandNo(String bandNo) {
        this.bandNo = bandNo;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getBandMobile() {
        return bandMobile;
    }

    public void setBandMobile(String bandMobile) {
        this.bandMobile = bandMobile;
    }

    public String getBandIdentityNo() {
        return bandIdentityNo;
    }

    public void setBandIdentityNo(String bandIdentityNo) {
        this.bandIdentityNo = bandIdentityNo;
    }



    private String accountID;   // 用户id
    private String accountType; // 电子账户类别   1对公，2对私
    private String accountName; // 电子账户名称  ，在运营网站中是用户名
    private String certType; 	// 证件类型 ，对私：1身份证。 对公：1组织机构代码，2营业执照，3组织代码（附属机构），4	其他证件类型
    private String certNo; 		// 证件号码
    private String mobile;		// 手机号码
    private String mobileCode;	// 短信验证码
    private String mobileSerial;// 验证码序号
    private String mainAccountNo;// 主账户  若我行商卡开户则直接填写商卡号
    private String mainAccountBankName;//支行名称
    private String mainAccountBranchNo; // 主账户人民银行联行行号
    private String eCardSerialNo;//协议号
    private String bankName;//银行卡名称

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String geteCardSerialNo() {
        return eCardSerialNo;
    }

    public void seteCardSerialNo(String eCardSerialNo) {
        this.eCardSerialNo = eCardSerialNo;
    }

    public String getMainAccountBankName() {
        return mainAccountBankName;
    }

    public void setMainAccountBankName(String mainAccountBankName) {
        this.mainAccountBankName = mainAccountBankName;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getMainAccountBranchNo() {
        return mainAccountBranchNo;
    }

    public void setMainAccountBranchNo(String mainAccountBranchNo) {
        this.mainAccountBranchNo = mainAccountBranchNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileCode() {
        return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
        this.mobileCode = mobileCode;
    }

    public String getMobileSerial() {
        return mobileSerial;
    }

    public void setMobileSerial(String mobileSerial) {
        this.mobileSerial = mobileSerial;
    }

    public String getMainAccountNo() {
        return mainAccountNo;
    }

    public void setMainAccountNo(String mainAccountNo) {
        this.mainAccountNo = mainAccountNo;
    }


    private String accountNo;//银行卡
//    private String accountName;//用户名
    private String identityNo;//身份证号
    private String singleLimit;//单笔交易额度
    private String dailyLimit;//每天交易额度
    private String totalLimit;//总交易额度
    private String collectionSerialNo;//代收协议号

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(String singleLimit) {
        this.singleLimit = singleLimit;
    }

    public String getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(String dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public String getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(String totalLimit) {
        this.totalLimit = totalLimit;
    }

    public String getCollectionSerialNo() {
        return collectionSerialNo;
    }

    public void setCollectionSerialNo(String collectionSerialNo) {
        this.collectionSerialNo = collectionSerialNo;
    }
}
