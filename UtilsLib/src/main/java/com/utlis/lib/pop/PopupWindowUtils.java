package com.utlis.lib.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.utlis.lib.R;


public class PopupWindowUtils {
	private Context context;
	private View view;

	private PopupWindow popupWindow;
	private PopupWindowUtils popupWindowUtils;
	private static int screen_width;
	private static int screen_height;

	public PopupWindowUtils(Context context) {
		this.context = context;
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;
		popupWindow = new PopupWindow(context);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		this.popupWindowUtils = this;
	}

	public View getContentView() {
		return this.view;
	}

	public PopupWindowUtils setContentView(View view) {
		this.view = view;
		if (view != null) {
			popupWindow.setContentView(view);
			popupWindow.setWidth(screen_width);
			popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		} else {
			popupWindow.setContentView(new TextView(context));
			popupWindow.setWidth(screen_width);
			popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		return popupWindowUtils;
	}

	public PopupWindowUtils setContentView(View view, int width) {
		this.view = view;
		if (view != null) {
			popupWindow.setContentView(view);
			popupWindow.setWidth(width);
			popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		} else {
			popupWindow.setContentView(new TextView(context));
			popupWindow.setWidth(width);
			popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		return popupWindowUtils;
	}

	public PopupWindowUtils setContentView(View view, int width, int height) {
		this.view = view;
		if (view != null) {
			popupWindow.setContentView(view);
			popupWindow.setWidth(width);
			popupWindow.setHeight(height);
		} else {
			popupWindow.setContentView(new TextView(context));
			popupWindow.setWidth(width);
			popupWindow.setHeight(height);
		}
		return popupWindowUtils;
	}

	public PopupWindowUtils setWidth(int width) {
		popupWindow.setWidth(width);
		return popupWindowUtils;
	}

	public PopupWindowUtils setHeight(int height) {
		popupWindow.setHeight(height);
		return popupWindowUtils;
	}

	public PopupWindowUtils setWidthAndHeight(int width, int height) {
		popupWindow.setWidth(width);
		popupWindow.setHeight(height);
		return popupWindowUtils;
	}
	float alpha = 1;
	public void show(View parent, int gravity, int x, int y) {
		if (popupWindow != null) {
			ColorDrawable cd = new ColorDrawable(0x000000);
			popupWindow.setBackgroundDrawable(cd);
			popupWindow.setAnimationStyle(R.style.popwin_anim_style);
//			WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//			lp.alpha = 0.8f;
//			((Activity) context).getWindow().setAttributes(lp);
			new Thread(new Runnable(){
				@Override
				public void run() {
					while(alpha>0.5f){
						try {
							//4是根据弹出动画时间和减少的透明度计算
							Thread.sleep(4);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						//每次减少0.01，精度越高，变暗的效果越流畅
						alpha-=0.01f;
						msg.obj =alpha ;
						mHandler.sendMessage(msg);
					}
				}

			}).start();
			popupWindow.update();
			popupWindow.setOnDismissListener(new poponDismissListener());
			popupWindow.showAtLocation(parent, gravity, x, y);
		}
	}

	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 1:
					backgroundAlpha((float)msg.obj);
					break;
			}
		}
	};

	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		((Activity) context).getWindow().setAttributes(lp);
		((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}
	/**
	 * 返回或者点击空白位置的时候将背景透明度改回来
	 */
	class poponDismissListener implements PopupWindow.OnDismissListener{

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				@Override
				public void run() {
					//此处while的条件alpha不能<= 否则会出现黑屏

					while(alpha<1f){
						try {
							Thread.sleep(4);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Message msg =mHandler.obtainMessage();
						msg.what = 1;
						alpha+=0.01f;
						msg.obj =alpha ;
						mHandler.sendMessage(msg);
					}
				}

			}).start();
		}

	}

	/**
	 * 向下弹出菜单界面
	 *
	 * @param anchor
	 *            出现在哪个view的下面
	 * @param xoff
	 * @param yoff
	 */
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		if (popupWindow != null) {
			ColorDrawable cd = new ColorDrawable(0x000000);
			popupWindow.setBackgroundDrawable(cd);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
			lp.alpha = 0.9f;
			((Activity) context).getWindow().setAttributes(lp);
			popupWindow.update();
			popupWindow.setOnDismissListener(new OnDismissListener() {
				public void onDismiss() {
					WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
					lp.alpha = 1f;
					((Activity) context).getWindow().setAttributes(lp);
				}
			});
			popupWindow.showAsDropDown(anchor, xoff, yoff);
		}
	}

	public void dismiss() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}
	}

	public static int getScreenWidth() {
		return screen_width;
	}

	public static int getScreenHeight() {
		return screen_height;
	}

}
