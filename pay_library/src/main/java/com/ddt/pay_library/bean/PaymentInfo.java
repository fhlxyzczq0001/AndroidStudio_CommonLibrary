package com.ddt.pay_library.bean;

/**
 * 汇元支付实体
 * 
 * @author hy
 * 
 */
public class PaymentInfo {
	// 支付初始化后返回的一个支付码 初始化才返回
	private String token_id;
	// 商家生成的订单号 初始化才回返回
	private String agent_bill_id;
	private String agent_id;
	private String pay_type;
	// 返回是否有误
	private boolean hasError;
	/**
	 * @return the token_id
	 */
	public String getToken_id() {
		return token_id;
	}
	/**
	 * @param token_id the token_id to set
	 */
	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}
	/**
	 * @return the agent_bill_id
	 */
	public String getAgent_bill_id() {
		return agent_bill_id;
	}
	/**
	 * @param agent_bill_id the agent_bill_id to set
	 */
	public void setAgent_bill_id(String agent_bill_id) {
		this.agent_bill_id = agent_bill_id;
	}
	/**
	 * @return the agent_id
	 */
	public String getAgent_id() {
		return agent_id;
	}
	/**
	 * @param agent_id the agent_id to set
	 */
	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	/**
	 * @return the pay_type
	 */
	public String getPay_type() {
		return pay_type;
	}
	/**
	 * @param pay_type the pay_type to set
	 */
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	/**
	 * @return the hasError
	 */
	public boolean isHasError() {
		return hasError;
	}
	/**
	 * @param hasError the hasError to set
	 */
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	
}

