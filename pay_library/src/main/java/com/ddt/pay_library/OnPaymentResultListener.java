package com.ddt.pay_library;

/**
 * Created by Administrator on 2016/12/6.
 *
 * @author lizhipei
 */

public interface OnPaymentResultListener {

    /**
     * @param OrderNumber   预发起支付的订单号
     * @param LotteryNumber 会员购买成功后返回的可抽奖次数
     */
    public void onPayOrderNumberListener(String OrderNumber,  String LotteryNumber);
    /**
     * 支付完成后的回调
     * @param isResult 支付结果
     * @param resultMsg 当前订单的订单号
     * @param LotteryNumber 会员购买成功后返回的可抽奖次数
     */
    public void onPaymentResultListener(boolean isResult, String resultMsg, String LotteryNumber);
}
