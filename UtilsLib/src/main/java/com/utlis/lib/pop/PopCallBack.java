package com.utlis.lib.pop;

/**
 * popupWindow弹窗关闭时的回调(返回类型自定义)
 *
 * @author Cong
 *
 */
public interface PopCallBack<E> {
	void onResult(E result);
}