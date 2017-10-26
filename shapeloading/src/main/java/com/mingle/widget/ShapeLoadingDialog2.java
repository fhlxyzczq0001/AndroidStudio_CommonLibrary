package com.mingle.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mingle.shapeloading.R;
import com.wang.avi.AVLoadingIndicatorView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by zzz40500 on 15/6/15.
 */
public class ShapeLoadingDialog2 {



    private Context mContext;
    private Dialog mDialog;
    private AVLoadingIndicatorView mLoadingView;
    private View mDialogContentView;
    private TextView msg;


    public ShapeLoadingDialog2(Context context) {
        this.mContext=context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_dialog);
        mDialogContentView= LayoutInflater.from(mContext).inflate(R.layout.layout_dialog2,null);


        mLoadingView= (AVLoadingIndicatorView) mDialogContentView.findViewById(R.id.indicator);
        msg= (TextView) mDialogContentView.findViewById(R.id.msg);
        mLoadingView.setIndicator("LineSpinFadeLoaderIndicator");
        mDialog.setContentView(mDialogContentView);
    }

    public void setBackground(int color){
        GradientDrawable gradientDrawable= (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence){
        if (TextUtils.isEmpty(charSequence)) {
            msg.setVisibility(GONE);
        } else {
            msg.setVisibility(VISIBLE);
        }
        msg.setText(charSequence);
    }

    public void show(){
        mDialog.show();

    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public Dialog getDialog(){
        return  mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        mDialog.setCanceledOnTouchOutside(cancel);
    }
}
