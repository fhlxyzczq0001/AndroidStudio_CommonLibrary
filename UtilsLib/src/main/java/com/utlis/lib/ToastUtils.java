package com.utlis.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author vondear 在系统的Toast基础上封装
 */

@SuppressLint("InflateParams")
public class ToastUtils {

    private static Toast toast = null;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;
    private static int LENGTH_SHORT = Toast.LENGTH_SHORT;

    /**
     * 普通文本消息提示
     * @param context
     * @param text
     * @param duration
     */
    public static void TextToast(Context context,CharSequence text,int duration){
        //创建一个Toast提示消息
        toast = Toast.makeText(context, text, duration);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
        //显示消息
        toast.show();
    }

    /**
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void RightImageToast(Context context,CharSequence text,String duration){
        //创建一个Toast提示消息

        int showtime;
        if(duration.equals("long")){

            showtime=Toast.LENGTH_LONG;

        }else{

            showtime=Toast.LENGTH_SHORT;

        }
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);

		img.setImageResource(R.mipmap.icon_toast_right);
		img.setPadding(1, 0, 10, 0);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static void RightImageToast(Context context,CharSequence text){
        //创建一个Toast提示消息

        int showtime;
        showtime=Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);

		img.setImageResource(R.mipmap.icon_toast_right);
		img.setPadding(1, 0, 10, 0);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }
    public static void RightImageToast(Context context,CharSequence text,int showtime){
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);

		img.setImageResource(R.mipmap.icon_toast_right);
		img.setPadding(1, 0, 10, 0);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                toast.cancel();
            }
        }, showtime);
    }

    public enum ToastGravity{
        TOP,CENTER,BOTTOM;
    }

    public static void RightImageToast(Context context,CharSequence text,ToastGravity toastGravity){
        //创建一个Toast提示消息

        int showtime;
        showtime=Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(getGravity(toastGravity), 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);

		img.setImageResource(R.mipmap.icon_toast_right);
		img.setPadding(1, 0, 10, 0);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }


    public static void ErrorImageToast(Context context,CharSequence text,String duration){
        //创建一个Toast提示消息
        int showtime;
        if(duration.equals("long")){

            showtime=Toast.LENGTH_LONG;

        }else{

            showtime=Toast.LENGTH_SHORT;

        }
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
		/*toast.setGravity(Gravity.CENTER, 0, 0);
		//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);
		img.setPadding(5, 0, 5, 0);
		img.setImageResource(R.mipmap.icon_toast_error);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static void ErrorImageToast(Context context,CharSequence text){
        //创建一个Toast提示消息
        int showtime;
        showtime=Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);
		img.setPadding(5, 0, 5, 0);
		img.setImageResource(R.mipmap.icon_toast_error);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static void ErrorImageToast(Context context,CharSequence text,ToastGravity toastGravity){
        //创建一个Toast提示消息
        int showtime;
        showtime=Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(getGravity(toastGravity), 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);
		img.setPadding(5, 0, 5, 0);
		img.setImageResource(R.mipmap.icon_toast_error);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static void WarnImageToast(Context context,CharSequence text,String duration){
        //创建一个Toast提示消息
        int showtime;
        if(duration.equals("long")){

            showtime=Toast.LENGTH_LONG;

        }else{

            showtime=Toast.LENGTH_SHORT;

        }
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);
		img.setPadding(5, 0, 5, 0);
		img.setImageResource(R.mipmap.icon_toast_warn);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static void WarnImageToast(Context context,CharSequence text){
        //创建一个Toast提示消息
        int showtime;
        showtime=Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.CENTER, 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);
		img.setPadding(5, 0, 5, 0);
		img.setImageResource(R.mipmap.icon_toast_warn);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static void WarnImageToast(Context context,CharSequence text,ToastGravity toastGravity){
        //创建一个Toast提示消息
        int showtime;
        showtime=Toast.LENGTH_LONG;
        toast = Toast.makeText(context, text, showtime);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(getGravity(toastGravity), 0, 0);
		/*//获取Toast提示消息里原有的View
		View toastView = toast.getView();
		//创建一个ImageView
		ImageView img = new ImageView(context);
		img.setPadding(5, 0, 5, 0);
		img.setImageResource(R.mipmap.icon_toast_warn);
		//创建一个LineLayout容器
		LinearLayout ll =(LinearLayout) toastView;
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER);
		//向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img,0);
		//ll.addView(toastView);
		//将LineLayout容器设置为toast的View
		toast.setView(ll);*/
        //显示消息
        toast.show();
    }

    public static int getGravity(ToastGravity toastGravity){
        if(toastGravity==ToastGravity.TOP){
            return Gravity.TOP;
        }else if(toastGravity==ToastGravity.CENTER){
            return Gravity.CENTER;
        }else if(toastGravity==ToastGravity.BOTTOM){
            return Gravity.BOTTOM;
        }
        return Gravity.CENTER;
    }
}
