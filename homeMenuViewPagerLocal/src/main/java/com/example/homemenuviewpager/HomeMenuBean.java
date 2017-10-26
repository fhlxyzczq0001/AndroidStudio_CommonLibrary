package com.example.homemenuviewpager;

import android.graphics.drawable.Drawable;

public class HomeMenuBean {
	private String menu_url; // 菜单跳转路径
	private String menu_icon; // 菜单图标
	private String menu_title; // 菜单标题
	private int menu_icon_drawable;//菜单图标
	private Drawable menu_top_right_drawable;//右上角图标

	public final Drawable getMenu_top_right_drawable() {
		return menu_top_right_drawable;
	}

	public final void setMenu_top_right_drawable(Drawable menu_top_right_drawable) {
		this.menu_top_right_drawable = menu_top_right_drawable;
	}

	public final int getMenu_icon_drawable() {
		return menu_icon_drawable;
	}

	public final void setMenu_icon_drawable(int menu_icon_drawable) {
		this.menu_icon_drawable = menu_icon_drawable;
	}

	public String getMenu_url() {
		return menu_url;
	}

	public void setMenu_url(String menu_url) {
		this.menu_url = menu_url;
	}

	public String getMenu_icon() {
		return menu_icon;
	}

	public void setMenu_icon(String menu_icon) {
		this.menu_icon = menu_icon;
	}

	public String getMenu_title() {
		return menu_title;
	}

	public void setMenu_title(String menu_title) {
		this.menu_title = menu_title;
	}
}
