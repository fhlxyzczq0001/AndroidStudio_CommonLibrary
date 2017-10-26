package com.utlis.lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

public class WindowUtils {

	/**
	 * 获取屏幕高度
	 * 
	 * @Title: getWindowHwight
	 * @Description: TODO
	 * @param context
	 * @return
	 * @return: int
	 */
	public static int getWindowHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return height;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @Title: getWindowWidth
	 * @Description: TODO
	 * @param context
	 * @return
	 * @return: int
	 */
	public static int getWindowWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return width;
	}

	/**
	 * 获取View高度
	 * 
	 * @Title: getViewHeight
	 * @Description: TODO
	 * @param v
	 * @return
	 * @return: int
	 */
	public static int getViewHeight(View v) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);

		int imgH = v.getMeasuredHeight();
		return imgH;
	}

	/**
	 * 获取控件宽
	 * 
	 * @Title: getViewWidth
	 * @Description: TODO
	 * @param v
	 * @return
	 * @return: int
	 */
	public static int getViewWidth(View v) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);

		int width = v.getMeasuredWidth();
		return width;
	}

	// --------------------------设置沉浸式状态栏----------------------------------
	@TargetApi(19)
	public static void setTranslucentStatus(Context context, boolean on) {
		Window win = ((Activity) context).getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	// --------------------------设置沉浸式状态栏----------------------------------
	public static void setSystemBarTintManager(Context context,View actionbarParent,int color ){
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 19) {

			int StatusHeight = WindowUtils.getStatusHeight((Activity) context);// 获取状态栏高度
			actionbarParent.setPadding(0, StatusHeight, 0, 0);// 设置最外层的padding

			setTranslucentStatus((Activity) context, true);
			SystemBarTintManager tintManager = new SystemBarTintManager(
					(Activity) context);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(color);// 状态栏所需颜色
		}
	}

	public static void setSystemBarTintManager(Context context,int color ){
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 19) {
			setTranslucentStatus((Activity) context, true);
			SystemBarTintManager tintManager = new SystemBarTintManager(
					(Activity) context);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(color);// 状态栏所需颜色
		}
	}


	/**
	 * 状态栏高度算法
	 * 
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
	/**
	 * 设置Actionbar的高度
	 */
	public static int setActionbarHeight(Context context,LinearLayout acbr_Parent_Layout ){
		int WindowWidth = getWindowWidth((Activity)context);// 获取屏幕宽度
		int WindowHeight = getWindowHeight((Activity)context);// 获取屏幕高度
		// 设置Actionbar的高度
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) acbr_Parent_Layout.getLayoutParams();
		layoutParams.width=WindowWidth;
		layoutParams.height=(int) (WindowHeight * 0.12);
		acbr_Parent_Layout.setLayoutParams(layoutParams);
		return layoutParams.height;
	}

	/**
	 * 获取 虚拟按键的高度
	 * @param context
	 * @return
	 */
	public static  int getBottomStatusHeight(Context context){
		int totalHeight = getDpi(context);

		int contentHeight = getScreenHeight(context);

		return totalHeight  - contentHeight;
	}
	//获取屏幕原始尺寸高度，包括虚拟功能键高度
	public static int getDpi(Context context){
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi=displayMetrics.heightPixels;
		}catch(Exception e){
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 获得屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
}
