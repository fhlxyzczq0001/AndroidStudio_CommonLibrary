package com.ddt.pay_library.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/6.
 * payLibrary的支付结果
 * @author lizhipei
 */

public class Payment_Result implements Parcelable{
    public static final Parcelable.Creator<Payment_Result> CREATOR = new Parcelable.Creator<Payment_Result>() {
        public Payment_Result createFromParcel(Parcel source) {
            return new Payment_Result(source);
        }

        public Payment_Result[] newArray(int size) {
            return new Payment_Result[size];
        }
    };
    public Payment_Result() {
    }

    public Payment_Result(String tradeType, String tradeNumber, String money, boolean state, String msg, String lotteryNumber) {
        this.tradeType = tradeType;
        this.tradeNumber = tradeNumber;
        this.money = money;
        this.state = state;
        this.msg = msg;
        this.lotteryNumber = lotteryNumber;
    }

    public Payment_Result(Parcel in) {
        tradeType = in.readString();
        msg = in.readString();
        money = in.readString();
        state = in.readByte()!=0;//myBoolean == true if byte != 0
        tradeNumber = in.readString();
        lotteryNumber = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tradeType);
        dest.writeString(money);
        dest.writeByte((byte)(state ?1:0));
        dest.writeString(tradeNumber);
        dest.writeString(msg);
        dest.writeString(lotteryNumber);
    }

    private String tradeType;//支付类型
    private String tradeNumber;//支付订单号
    private String money;//支付金额
    private boolean state;//支付结果状态
    private String msg;//支付结果信息
    private String lotteryNumber;//购买会员后，会员可抽奖次数

    public String getLotteryNumber() {
        return lotteryNumber;
    }

    public void setLotteryNumber(String lotteryNumber) {
        this.lotteryNumber = lotteryNumber;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getMoney() {
        return money;
    }

    public boolean isState() {
        return state;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public String getMsg() {
        return msg;
    }

    public static Parcelable.Creator<Payment_Result> getCREATOR() {
        return CREATOR;
    }
}
