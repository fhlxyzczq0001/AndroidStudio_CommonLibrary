package com.ddt.pay_library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.ddt.pay_library.bean.JDRechargeBean;
import com.ddt.pay_library.bean.JHFBean;
import com.ddt.pay_library.bean.PaymentInfo;
import com.ddt.pay_library.bean.WXRechargeBean;
import com.heepay.plugin.api.HeepayPlugin;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.jhpay.sdk.JHpayInterface;
import com.jhpay.sdk.entities.Card;
import com.jhpay.sdk.util.Constants;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wangyin.wepay.TradeInfo;
import com.wangyin.wepay.WePay;
//import com.mtdl.dlpaysdk.activity.DLPayManager;
import java.util.HashMap;

import jspy.weixin.pay.entity.Order;
import jspy.weixin.pay.task.JshyPay;
import jspy.weixin.pay.task.JshyUpData;

import static com.alibaba.fastjson.JSON.parseObject;


/**
 * Created by Administrator on 2016/12/2.
 *
 * @author lizhipei
 */

public class PayManageUtils {

    //如果是购买会员，则返回会员可抽奖次数
    String lotteryNumber = "";
    /**
     * 支付结果回调接口
     */
    OnPaymentResultListener onPaymentResultListener;

    private volatile static PayManageUtils singleton;

    private PayManageUtils() {
    }


    public static PayManageUtils getInstance(){
        if(singleton==null){
            synchronized(PayManageUtils.class){
                if(singleton==null){
                    singleton=new PayManageUtils();
                }
            }
        }
        return singleton;
    }

    /**
     * 请求支付
     *  网页的统一在第三方支付的webview中加载
     * @TODO 目前缺少梓微兴
     * @param tradeType 支付类型
     * @param money     支付金额
     * @param orderInfo 后台生成的订单信息
     * @param payStatus 支付类型
     */
    public void sendPay(Activity activity, String tradeType, String money, String orderInfo, String payStatus, OnPaymentResultListener onPaymentResultListener){
        this.onPaymentResultListener = onPaymentResultListener;
        if(orderInfo == null || orderInfo.equals("")){
            onPaymentResultListener.onPaymentResultListener(false, "未支付", "");
            return;
        }
        //接收到的json对象
        JSONObject orderJson = parseObject(orderInfo);
        //如果是购买会员，返回会员可抽奖次数
        if(payStatus != null && !payStatus.equals("") && payStatus.equals("member")){
            lotteryNumber = orderJson.getString("lotteryNumber");
        }
        if(tradeType.equals(TradeType_Code.WX_APP)){//微信原生支付
            wx_app(activity, orderJson, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.ZFB_APP1)){//支付宝原生支付
            String result_json = orderJson.getString("result_json");
            String tradeNumber = orderJson.getString("tradeNumber");
            zfbAppPay(activity, result_json, tradeNumber, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.ZFB_WAP1)){//支付宝wap支付
            String url = orderJson.getString("recharge_url");//充值url
            Intent intent = new Intent(activity, H5PayActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("barname", "第三方支付");
            activity.startActivityForResult(intent, 10012);
        }else if(tradeType.equals(TradeType_Code.IAPP_APK)){//爱贝支付
            iAppPay(activity, orderJson, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.CMB_H5)){//招行支付
            cmbPay(activity, orderJson, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.JD_APP)){//京东支付
            JDRechargeBean jdRechargeBean = parseObject(orderJson.getString("result_json"), JDRechargeBean.class);
            String tradeNumber = orderJson.getString("tradeNumber");
            jdPay(activity, jdRechargeBean, tradeNumber, lotteryNumber);
        }else if(tradeType.contains("JHF") && tradeType.contains("APP")){//包含JHF，判定为聚合富支付，聚合富包含微信、支付宝、收银台三种模式
            int type = 0;// 默认类型为0，收银台模式
            if (tradeType.equals(TradeType_Code.JHF_WX_APP)) {
                // 判断如果充值类型为JHF_WX_APP,直接调聚合富微信支付
                type = 1;
            } else if (tradeType.equals(TradeType_Code.JHF_ZFB_APP)) {
                // 判断如果充值类型为JHF_ZFB_APP,直接调聚合富支付宝支付
                type = 2;
            }
            // 充值订单号
            String tradeNumber = orderJson.getString("tradeNumber");
            String result_json = orderJson.getString("result_json");
            JHFBean payBean = parseObject(result_json, JHFBean.class);
            startJHFPay(activity, payBean, type, tradeNumber, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.WFT_WX_APP)//威富通微信
                || tradeType.equals(TradeType_Code.WFT_WX_WAP)//威富通微信wap支付
                || tradeType.contains("WFT_WX")){//威富通支付
            // 充值订单号
            String tradeNumber = orderJson.getString("tradeNumber");
            String result_json = orderJson.getString("result_json");
            String appID = orderJson.getString("appID");
            wftPay(activity, result_json, tradeType, money, tradeNumber, appID, lotteryNumber);
        }else if(tradeType.contains("AY_WX_APK")){//爱益微信支付
            String result_json = orderJson.getString("result_json");
            String tradeNumber = orderJson.getString("tradeNumber");
            ayPay(activity, result_json, tradeNumber, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.HY_WX_APK) //汇元支付
                || tradeType.equals(TradeType_Code.HY_WX_APP_APK) //汇元微信支付
                || tradeType.equals(TradeType_Code.HY_ZFB_APP_APK)){ //汇元支付宝支付
            startHeepayService(activity, orderJson, lotteryNumber);
        }else if(tradeType.equals(TradeType_Code.MTDL)){//明天动力支付
            mtdlPay(activity);
        }else{//未知类型，跳转网页处理
            //可处理：汇潮、乐付、布优
            String url = orderJson.getString("recharge_url");//充值url
            Intent intent = new Intent(activity, WebView_RechargeAct.class);
            intent.putExtra("url", url);
            intent.putExtra("barname", "第三方支付");
            activity.startActivityForResult(intent, 10011);
        }
    }

    /**
     * 原生支付宝支付
     * @param activity
     * @param orderInfo   订单信息
     * @param lotteryNumber
     */
    private void zfbAppPay(final Activity activity, final String orderInfo, final String tradeNumber, final String lotteryNumber) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
                String result = alipay.pay(orderInfo, true);
                //resultStatus={6001};memo={用户取消};result={}
//                PayResult payResult = JSONObject.parseObject(result, PayResult.class);
                String tag = "resultStatus={";
                int indexT = result.indexOf(tag);
                int indexB = result.indexOf("}", indexT);
                String resultStatus = result.substring(indexT+tag.length(), indexB);
//                String resultStatus = payResult.getResultStatus();
                if(onPaymentResultListener != null){
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        onPaymentResultListener.onPaymentResultListener(true, ""+tradeNumber, ""+lotteryNumber);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        onPaymentResultListener.onPaymentResultListener(false, "未支付", ""+lotteryNumber);
                    }
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
        return;
    }

    /**
     * 爱贝支付
     */
    private void iAppPay(final Activity app, JSONObject params, final String lotteryNumber) {
        JSONObject json;
        try {
            final String tradeNumber = params.getString("tradeNumber");
            json = JSONObject.parseObject(params.getString("result_json"));
            // 爱贝支付初始化，从后台取回appid并初始化
            IAppPay.init(app, json.getString("appid"));
            String transid_appid = "transid=" + json.getString("transid") + "&appid="
                    + json.getString("appid");
            IAppPay.startPay(app, transid_appid, new IPayResultCallback() {
                @Override
                public void onPayResult(int arg0, String arg1, String arg2) {
                    if(onPaymentResultListener != null){
                        if (arg0 == IAppPay.PAY_SUCCESS) {
                            onPaymentResultListener.onPaymentResultListener(true, ""+tradeNumber, ""+lotteryNumber);
                        } else {
                            onPaymentResultListener.onPaymentResultListener(false, "未支付", ""+lotteryNumber);
                        }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            if(onPaymentResultListener != null){
                onPaymentResultListener.onPaymentResultListener(false, "订单信息错误，请联系管理员", "");
            }
        }
    }

    /**
     * 京东支付1.0
     *
     * @param activity
     *            activity
     * @param jdRechargeBean
     *            京东充值对象
     */
    private void jdPay(Activity activity, JDRechargeBean jdRechargeBean, String tradeNumber,  String lotteryNumber) {
        if(onPaymentResultListener != null){
            onPaymentResultListener.onPayOrderNumberListener(tradeNumber, lotteryNumber);
        }
        TradeInfo tradeInfo = new TradeInfo();
        // 用户交易令牌
        tradeInfo.token = jdRechargeBean.getToken();
        tradeInfo.merchantRemark = jdRechargeBean.getMerchantRemark();
        tradeInfo.merchantJdPin = jdRechargeBean.getMerchantJdPin();
        // 商户账户体系的用户ID
        tradeInfo.merchantUserId = jdRechargeBean.getMerchantUserId();
        // 用户在商户的手机号
        tradeInfo.merchantMobile = jdRechargeBean.getMerchantMobile();
        // 商户号 （必填）
        tradeInfo.merchantNum = jdRechargeBean.getMerchantNum();
        // 交易流水号（必填）
        tradeInfo.tradeNum = jdRechargeBean.getTradeNum();
        // 商品名称（必填）
        tradeInfo.tradeName = jdRechargeBean.getTradeName();
        // 交易描述
        tradeInfo.tradeDescription = jdRechargeBean.getTradeDescription();
        // 交易时间（必填）
        tradeInfo.tradeTime = jdRechargeBean.getTradeTime();
        // 交易金额（必填,单位分）
        tradeInfo.tradeAmount = jdRechargeBean.getTradeAmount();
        // 货币种类（必填："CNY"）
        tradeInfo.currency = jdRechargeBean.getCurrency();
        // // 异步通知页面地址（必填）
        tradeInfo.notifyUrl = jdRechargeBean.getNotifyUrl();
        // 用户交易信息签名（必填）
        tradeInfo.merchantSign = jdRechargeBean.getMerchantSign();
        // 发起请求，调用网银+
        String message = WePay.pay(activity, tradeInfo, 1);
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(activity, message+"---"+tradeNumber, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析后台返回的支付订单信息
     *         返回的字段为小写，
     * 充值类型为WX_APP
     * @param context
     *            当前上下文
     * @param rootJson
     */
    private void wx_app(Activity context, JSONObject rootJson, String lotteryNumber) {
        // 充值订单号
        String tradeNumber = rootJson.getString("tradeNumber");
        String result_json = rootJson.getString("result_json");
        try {
            JSONObject jsonObject = parseObject(result_json);
            WXRechargeBean wxRechargeBean = new WXRechargeBean();
            wxRechargeBean.setAppId(jsonObject.getString("appid"));
            wxRechargeBean.setPartnerid(jsonObject.getString("partnerid"));
            wxRechargeBean.setPrepayid(jsonObject.getString("prepayid"));
            wxRechargeBean.setNoncestr(jsonObject.getString("noncestr"));
            wxRechargeBean.setTimestamp(jsonObject.getString("timestamp"));
            wxRechargeBean.setSign(jsonObject.getString("sign"));
            wxRechargeBean.setPackageValue(jsonObject.getString("package"));
            wxAppPay(context, wxRechargeBean, tradeNumber, lotteryNumber);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            if(onPaymentResultListener != null){
                onPaymentResultListener.onPaymentResultListener(false, "订单信息错误，请联系管理员", "");
            }
        }

    }

    /**
     * 调起微信原生支付
     * @param context
     * @param rechargeBean
     */
    private void wxAppPay(Context context, WXRechargeBean rechargeBean, String tradeNumber, String lotteryNumber) {
       IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        PayReq req = new PayReq();
        req.appId = rechargeBean.getAppId();
        req.partnerId = rechargeBean.getPartnerid();
        req.prepayId = rechargeBean.getPrepayid();
        req.packageValue = rechargeBean.getPackageValue();
        req.nonceStr = rechargeBean.getNoncestr();
        req.timeStamp = rechargeBean.getTimestamp();
        req.sign = rechargeBean.getSign();
        boolean sendSuccess = msgApi.registerApp(req.appId);
        sendSuccess =  msgApi.sendReq(req);
        if(!sendSuccess){
            onPaymentResultListener.onPaymentResultListener(false, "", "");
        }else if(onPaymentResultListener != null){
            onPaymentResultListener.onPayOrderNumberListener(""+tradeNumber, ""+lotteryNumber);
        }
    }

    /**
     * 聚合富支付
     *
     * @param activity
     *            当前activity
     * @param type
     *            充值类型 2：调用支付宝支付 1：调用微信支付 0：启动带有选择支付方式支付
     */
    private void startJHFPay(Activity activity, JHFBean jhfBean, int type, String tradeNumber, String lotteryNumber) {
        if(onPaymentResultListener != null){
            onPaymentResultListener.onPayOrderNumberListener(""+tradeNumber, ""+lotteryNumber);
        }
        HashMap<String, String> ht = new HashMap<String, String>();
        ht.put(Constants.VERSION, jhfBean.getVersion());// 插件默认，无需改动
        ht.put(Constants.MERID, jhfBean.getMerid());// 商户编码
        ht.put(Constants.MERNAME, jhfBean.getMername());// 商户名称
        ht.put(Constants.POLICYID, jhfBean.getPolicyid());// 默认，无需改动
        ht.put(Constants.MERORDERID, jhfBean.getMerorderid());// 商户订单号，商户系统生成的唯一订单号
        ht.put(Constants.PAYMONEY, jhfBean.getPaymoney());// 支付金额，单位为“分”，请填入大于等于1的整数
        ht.put(Constants.PRODUCTNAME, jhfBean.getProductname());// 商品名称
        ht.put(Constants.PRODUCTDESC, jhfBean.getProductdesc());// 产品描述
        ht.put(Constants.USERID, jhfBean.getUserid());// 非空
        ht.put(Constants.USERNAME, jhfBean.getUsername());// 非空
        ht.put(Constants.EMAIL, jhfBean.getEmail());// 非空
        ht.put(Constants.PHONE, jhfBean.getPhone());// 非空
        ht.put(Constants.EXTRA, jhfBean.getExtra());// 附加信息
        ht.put(Constants.CUSTOM, jhfBean.getCustom());// 商户定制信息
        // MAC加密串
        ht.put(Constants.MD5, jhfBean.getMd5());// MD5校验值
        if (type == 1) {
            // 调用微信支付
            JHpayInterface.startPaymentNoSelectionInterface(activity, ht, Card.Type_WX);
        } else if (type == 2) {
            // 调用支付宝支付
            JHpayInterface.startPaymentNoSelectionInterface(activity, ht, Card.Type_Ali);
        } else {
            // 启动带有选择支付方式的界面
            JHpayInterface.startPayment(activity, ht);
        }
    }

    /**
     * 威富通app
     *
     * @param activity
     *            activity
     * @param result_json
     *            支付token
     * @param pay_type
     *            支付类型
     * @param result_money
     *            支付金额
     * @param tradeNumber
     *            交易订单号
     */
    private void wftPay(Activity activity, String result_json, String pay_type,
                               String result_money, String tradeNumber, String appID, String lotteryNumber) {
        if(onPaymentResultListener != null){
            onPaymentResultListener.onPayOrderNumberListener(""+tradeNumber, ""+lotteryNumber);
        }
        RequestMsg msg = new RequestMsg();
        if (pay_type.contains("WX_APP") || pay_type.contains("WX_APK")) {
            msg.setTokenId(result_json);
            msg.setTradeType(MainApplication.WX_APP_TYPE);
            msg.setAppId(appID);
            PayPlugin.unifiedAppPay(activity, msg);
        } else {
            msg.setMoney(Double.parseDouble(result_money));
            msg.setTokenId(result_json);
            msg.setOutTradeNo(tradeNumber);
            msg.setTradeType(MainApplication.PAY_WX_WAP);
            PayPlugin.unifiedH5Pay(activity, msg);
        }
    }

    /**
     * 汇付宝支付
     *
     * @param activity
     *            应用上下文
     * @param json
     *            返回json数据
     */
    private void startHeepayService(Activity activity, JSONObject json, String lotteryNumber) {
        try {
            String result_json = json.getString("result_json");
            String tradeNumber = json.getString("tradeNumber");
            if(onPaymentResultListener != null){
                onPaymentResultListener.onPayOrderNumberListener(""+tradeNumber, ""+lotteryNumber);
            }
            PaymentInfo _paymentInfo = JSON.parseObject(result_json, PaymentInfo.class);
            HeepayPlugin.pay(activity,
                    _paymentInfo.getToken_id() + "," + _paymentInfo.getAgent_id() + ","
                            + _paymentInfo.getAgent_bill_id() + "," + _paymentInfo.getPay_type());
        } catch (JSONException e) {
            e.printStackTrace();
            if(onPaymentResultListener != null){
                onPaymentResultListener.onPaymentResultListener(false, "订单信息错误，请联系管理员", "");
            }
        }
    }

    /**
     * 招行支付
     * @param activity
     * @param orderJson
     */
    private void cmbPay(Activity activity, JSONObject orderJson, String lotteryNumber){
        // 充值订单号
        String tradeNumber = orderJson.getString("tradeNumber");
        String url = orderJson.getString("recharge_url");
        if(onPaymentResultListener != null){
            onPaymentResultListener.onPayOrderNumberListener(""+tradeNumber, ""+lotteryNumber);
        }
        Intent intent = new Intent(activity, WebViewCMBAct.class);
        intent.putExtra("url", url);
        activity.startActivityForResult(intent, 10013);
    }

    /**
     * 爱益支付
     */
    private void ayPay(Activity fromActivity, String json, String tradeNumber, String lotteryNumber){
        com.alibaba.fastjson.JSONObject jsonOrder = com.alibaba.fastjson.JSONObject.parseObject(json);
        JshyPay.initSDK(fromActivity);
        Order mOrder=Order.getInstance();
        // APP id
        mOrder.setApp_id(""+jsonOrder.getString("appId"));
        // 计费说明
        mOrder.setBody(""+jsonOrder.getString("body"));
        //设备信息
        mOrder.setDevice_info(""+jsonOrder.getString("deviceInfo"));
        //异步回调地址
        mOrder.setNofity_url(""+jsonOrder.getString("notifyUrl"));
        //订单号
        mOrder.setPara_tradeNo(""+jsonOrder.getString("orderId"));
        //商户 id
        mOrder.setPara_id(""+jsonOrder.getString("paraId"));
        // 金额
        mOrder.setTotal_fee(""+jsonOrder.getString("totalFree"));
        //自定义参数
        mOrder.setAttach(""+jsonOrder.getString("attach"));
        mOrder.setSign(""+jsonOrder.getString("sign"));
        new JshyUpData().execute(mOrder);
        if(onPaymentResultListener != null){
            onPaymentResultListener.onPayOrderNumberListener(""+tradeNumber, ""+lotteryNumber);
        }
    }

    /**
     * 明天动力支付
     * @param mContext
     */
    private void mtdlPay(Activity mContext){
//        DLPayManager.isDebugOn(true);
//        DLPayManager manager = DLPayManager.getInstance(mContext,"0000000022");
//        manager.startDLPaysdk();
    }
}
