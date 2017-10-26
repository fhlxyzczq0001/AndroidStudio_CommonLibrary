package com.utlis.lib;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

/**
 * @ClassName: com.ygworld.Util
 * @author: Administrator 杨重诚
 * @date: 2016/8/30:17:29
 */
public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;
    private String des = "";//"s后重试"描述
    private String finishDes = "";//"点击重新获取"描述
    CountDownFinishListener mDownFinishListener;//倒计时结束回调
    OnCountDownTickListener onCountDownTickListener;//倒计时剩余时间的回调

    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval, String des, String finishDes) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.des = des;
        this.finishDes = finishDes;
    }

    public CountDownTimerUtils(TextView textView, long millisInFuture, String des, String finishDes) {
        super(millisInFuture, 1000);
        this.mTextView = textView;
        this.des = des;
        this.finishDes = finishDes;
    }

    public CountDownTimerUtils(TextView textView, long millisInFuture, String des, CountDownFinishListener mDownFinishListener) {
        super(millisInFuture, 1000);
        this.mTextView = textView;
        this.mDownFinishListener = mDownFinishListener;
        this.des = des;
    }

    public CountDownTimerUtils(long millisInFuture, CountDownFinishListener mDownFinishListener) {
        super(millisInFuture, 1000);
        this.mDownFinishListener = mDownFinishListener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(onCountDownTickListener != null){
            onCountDownTickListener.onTick(millisUntilFinished);
        }
        if (mTextView != null) {
            mTextView.setClickable(false); //设置不可点击
            mTextView.setText(des + "（" + millisUntilFinished / 1000 + "s）");  //设置倒计时时间

            /**
             * 超链接 URLSpan
             * 文字背景颜色 BackgroundColorSpan
             * 文字颜色 ForegroundColorSpan
             * 字体大小 AbsoluteSizeSpan
             * 粗体、斜体 StyleSpan
             * 删除线 StrikethroughSpan
             * 下划线 UnderlineSpan
             * 图片 ImageSpan
             * http://blog.csdn.net/ah200614435/article/details/7914459
             */
            SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
            ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
            /**
             * public void setSpan(Object what, int start, int end, int flags) {
             * 主要是start跟end，start是起始位置,无论中英文，都算一个。
             * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
             */
            spannableString.setSpan(span, 0, mTextView.getText().toString().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
            mTextView.setText(spannableString);
        }
    }

    @Override
    public void onFinish() {
        if (mTextView != null) {
            SpannableString content = new SpannableString(finishDes);
            content.setSpan(new UnderlineSpan(), 0, finishDes.length(), 0);
            mTextView.setText(content);
            mTextView.setClickable(true);//重新获得点击
        }
        stop();
        if (mDownFinishListener != null) {
            mDownFinishListener.onResult(true);
        }
    }

    public void setOnCountDownTickListener(OnCountDownTickListener onCountDownTickListener) {
        this.onCountDownTickListener = onCountDownTickListener;
    }

    /**
     * 倒计时结束的回调监听
     */
    public interface CountDownFinishListener {
        public void onResult(boolean finish);
    }

    /**
     * 倒计时剩余时间的监听
     */
    public interface OnCountDownTickListener {
        public void onTick(long millisUntilFinished);
    }

    public void stop() {
        cancel();
    }
}
