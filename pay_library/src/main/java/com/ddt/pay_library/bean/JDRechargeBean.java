package com.ddt.pay_library.bean;

public class JDRechargeBean {

	private String currency;// 货币种类（必填："CNY"）
	private String merchantMobile;// 用户在商户的手机号
	private String merchantNum;// 商户号 （必填）
	private String merchantRemark;	
	private String merchantSign;// 用户交易信息签名（必填）
	private String merchantUserId;	// 商户账户体系的用户ID
	private String notifyUrl;// 异步通知页面地址（必填）
	private String token;	// 用户交易令牌
	private long tradeAmount;// 交易金额（必填,单位分）
	private String tradeDescription;// 交易描述
	private String tradeName;// 商品名称（必填）
	private String tradeNum;// 交易流水号（必填）
	private String tradeTime;// 交易时间（必填）
	private String merchantJdPin;
	public String getMerchantJdPin() {
		return merchantJdPin;
	}
	public void setMerchantJdPin(String merchantJdPin) {
		this.merchantJdPin = merchantJdPin;
	}
	public String getMerchantRemark() {
		return merchantRemark;
	}
	public void setMerchantRemark(String merchantRemark) {
		this.merchantRemark = merchantRemark;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMerchantUserId() {
		return merchantUserId;
	}
	public void setMerchantUserId(String merchantUserId) {
		this.merchantUserId = merchantUserId;
	}
	public String getMerchantMobile() {
		return merchantMobile;
	}
	public void setMerchantMobile(String merchantMobile) {
		this.merchantMobile = merchantMobile;
	}
	public String getMerchantNum() {
		return merchantNum;
	}
	public void setMerchantNum(String merchantNum) {
		this.merchantNum = merchantNum;
	}
	public String getTradeNum() {
		return tradeNum;
	}
	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getTradeDescription() {
		return tradeDescription;
	}
	public void setTradeDescription(String tradeDescription) {
		this.tradeDescription = tradeDescription;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public long getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(long tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getMerchantSign() {
		return merchantSign;
	}
	public void setMerchantSign(String merchantSign) {
		this.merchantSign = merchantSign;
	}

	public String toString(){
		return "jdRechargeBean:[currency:"+currency+",merchantMobile:"+merchantMobile+",merchantNum:"+merchantNum+",merchantRemark:"+merchantRemark
				+",merchantSign:"+merchantSign+",merchantUserId:"+merchantUserId+",notifyUrl:"+notifyUrl+",token:"+token+",tradeAmount:"+tradeAmount
				+",tradeDescription:"+tradeDescription+",tradeName:"+tradeName+",tradeNum:"+tradeNum+",tradeTime:"+tradeTime;
	}
}
