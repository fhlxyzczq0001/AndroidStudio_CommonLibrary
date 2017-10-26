package com.ddt.pay_library.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/5.
 * 调起第三方支付时需传递的实体对象
 * 请求时所有参数不可为空
 * @author lizhipei
 */

public class Basic_Bean implements Parcelable{

    public static final Creator<Basic_Bean> CREATOR = new Creator<Basic_Bean>() {
        public Basic_Bean createFromParcel(Parcel source) {
            return new Basic_Bean(source);
        }

        public Basic_Bean[] newArray(int size) {
            return new Basic_Bean[size];
        }
    };
    public Basic_Bean() {
    }

    public Basic_Bean(Parcel in) {
        tradeType = in.readString();
        money = in.readString();
        orderInfo = in.readString();
        payStatus = in.readString();
//        tradeNumber = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeType);
        dest.writeString(money);
        dest.writeString(orderInfo);
        dest.writeString(payStatus);
//        dest.writeString(tradeNumber);
    }

    private String tradeType;//支付类型
//    private String tradeNumber;//支付订单号
    private String money;//支付金额
    private String orderInfo;//支付订单信息
    private String payStatus; //支付类型  （购物车支付、充值、会员支付）

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

//    public void setTradeNumber(String tradeNumber) {
//        this.tradeNumber = tradeNumber;
//    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }
    public String getTradeType() {
        return tradeType;
    }

    public String getMoney() {
        return money;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public static Creator<Basic_Bean> getCREATOR() {
        return CREATOR;
    }
}
