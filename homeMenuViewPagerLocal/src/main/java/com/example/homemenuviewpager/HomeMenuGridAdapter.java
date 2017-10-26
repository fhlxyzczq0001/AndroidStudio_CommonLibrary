package com.example.homemenuviewpager;

import java.util.ArrayList;
import java.util.List;

import com.example.homemenuviewpager.MenuView.ConfigItemListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

public class HomeMenuGridAdapter extends BaseAdapter {

	private Context mContext;
	List<HomeMenuBean> menuList = new ArrayList<HomeMenuBean>();
	// List<HomeType_Bean> menuList = new ArrayList<HomeType_Bean>();
	private ConfigItemListener configItemListener;
	private int pageSize = 8;// 每页总共显示的图片数量
	private int gridViewNumColumns;
	private double imgWidthRate;
	private boolean isShowSpacing;
	private double topImgWidthRate;

	public HomeMenuGridAdapter(Context context, int gridViewNumColumns,
			boolean isShowSpacing, double imgWidthRate,
			ConfigItemListener configItemListener,double topImgWidthRate) {
		this.mContext = context;
		this.gridViewNumColumns = gridViewNumColumns;
		this.topImgWidthRate = topImgWidthRate;
		if (imgWidthRate > 0 && imgWidthRate <= 1) {
			this.imgWidthRate = imgWidthRate;
		} else {
			this.imgWidthRate = 0.1;
		}
		this.isShowSpacing = isShowSpacing;
		this.configItemListener = configItemListener;
	}

	public HomeMenuGridAdapter(Context context, int gridViewNumColumns,
			boolean isShowSpacing, double imgWidthRate,double topImgWidthRate) {
		this(context, gridViewNumColumns, isShowSpacing, imgWidthRate, null,topImgWidthRate);
	}

	@Override
	public int getCount() {
		if (null != menuList && menuList.size() > 0) {
			return menuList.size();
		}
		return 0;
	}

	public void setData(List<HomeMenuBean> menuList) {
		this.menuList = menuList;
	}

	@Override
	public Object getItem(int position) {

		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.act_intro_menu_item, null);
			viewHolder.menu_item = (LinearLayout) convertView
					.findViewById(R.id.menu_item);
			viewHolder.goodsImage = (ImageView) convertView
					.findViewById(R.id.menu_item_image);
			viewHolder.topRightImage = (ImageView) convertView
					.findViewById(R.id.top_right_image);
			viewHolder.goodsTitle = (TextView) convertView
					.findViewById(R.id.menu_item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		HomeMenuBean menuBean = menuList.get(position);
		// 设置item宽高
		int width = (int) (getWindowWidth(mContext) / gridViewNumColumns);
		LayoutParams itemParams = viewHolder.menu_item.getLayoutParams();
		itemParams.height = width;
		itemParams.width = width;
		viewHolder.menu_item.setLayoutParams(itemParams);
		if (isShowSpacing) {
			viewHolder.menu_item.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.gridview_divider));
		}
		if (null != menuBean.getMenu_top_right_drawable()) {
			// 设置图片宽高
			int topRightImageWidth = (int) (getWindowWidth(mContext) / gridViewNumColumns*topImgWidthRate);// 获取屏幕宽度的10分之一
			RelativeLayout.LayoutParams topRightImageParams = (android.widget.RelativeLayout.LayoutParams) viewHolder.topRightImage
					.getLayoutParams();
			topRightImageParams.height = (int) (topRightImageWidth);
			topRightImageParams.width = (int) (topRightImageWidth);
			viewHolder.topRightImage.setLayoutParams(topRightImageParams);
			viewHolder.topRightImage.setBackgroundDrawable(menuBean
					.getMenu_top_right_drawable());
		}
		// 设置图片宽高
		int imgWidth = (int) (getWindowWidth(mContext) * imgWidthRate);// 获取屏幕宽度的10分之一
		LinearLayout.LayoutParams imgParams = (android.widget.LinearLayout.LayoutParams) viewHolder.goodsImage
				.getLayoutParams();
		imgParams.height = imgWidth;
		imgParams.width = imgWidth;
		viewHolder.goodsImage.setLayoutParams(imgParams);
		if (null != configItemListener) {
			String imgUrl = menuBean.getMenu_icon();
			if (null != imgUrl && !"".equals(imgUrl.trim())) {
				this.configItemListener.configItemImage(viewHolder.goodsImage,
						imgUrl);
			} else if (0 != menuBean.getMenu_icon_drawable()) {
				viewHolder.goodsImage.setBackgroundResource(menuBean
						.getMenu_icon_drawable());
			}
			this.configItemListener.configItemText(viewHolder.goodsTitle);
		} else {
			if (0 != menuBean.getMenu_icon_drawable()) {
				viewHolder.goodsImage.setBackgroundResource(menuBean
						.getMenu_icon_drawable());
			}
		}
		viewHolder.goodsTitle.setText("" + menuBean.getMenu_title());
		return convertView;
	}

	public class ViewHolder {
		public LinearLayout menu_item;
		public ImageView goodsImage;
		public ImageView topRightImage;
		public TextView goodsTitle;
	}

	/**
	 * 获取屏幕宽度
	 */
	public int getWindowWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return width;
	}
}
