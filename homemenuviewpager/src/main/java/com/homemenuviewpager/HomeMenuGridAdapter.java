package com.homemenuviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homemenuviewpager.MenuView.ConfigItemListener;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuGridAdapter extends BaseAdapter {

	private Context mContext;
	List<HomeMenuBean> menuList = new ArrayList<HomeMenuBean>();
//	List<HomeType_Bean> menuList = new ArrayList<HomeType_Bean>();
	private ConfigItemListener configItemListener;
	private int pageSize=8;//每页总共显示的图片数量
	
	public HomeMenuGridAdapter(Context context, ConfigItemListener configItemListener) {
		this.mContext = context;
		this.configItemListener = configItemListener;
	}
  

	@Override
	public int getCount() {
		if (null != menuList && menuList.size()>0) {
			return menuList.size();
		}
		return 0;
	}

	public void setData(List<HomeMenuBean> menuList){
		this.menuList = menuList;
	}
//	public void setData(List<HomeType_Bean> menuList){
//		this.menuList = menuList;
//	}
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
		if(convertView == null){
			viewHolder = new ViewHolder();
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.act_intro_menu_item, null);
			viewHolder.menu_item = (LinearLayout)convertView.findViewById(R.id.menu_item);
			viewHolder.goodsImage= (ImageView)convertView.findViewById(R.id.menu_item_image);
			viewHolder.goodsTitle = (TextView) convertView.findViewById(R.id.menu_item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		HomeMenuBean menuBean = menuList.get(position);
//		HomeType_Bean menuBean = menuList.get(position);
		
		if (null != configItemListener) {
			String imgUrl = menuBean.getMenu_icon();
//			String imgUrl = menuBean.getIcon();
			
			int width= (int) (getWindowWidth(mContext)* 0.1);// 获取屏幕宽度的10分之一
			LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) viewHolder.goodsImage.getLayoutParams();
			params.height=width;
			params.width=width;
			viewHolder.goodsImage.setLayoutParams(params);
			viewHolder.goodsTitle.setText(""+menuBean.getMenu_title());
			viewHolder.goodsTitle.setTag(Integer.toString(position)+menuBean.isSelect());
			this.configItemListener.configItemImage(viewHolder.goodsImage, imgUrl);
			this.configItemListener.configItemText(viewHolder.goodsTitle);

//			viewHolder.goodsTitle.setText(""+menuBean.getTitle());
		}
		
		return convertView;
	}

	public class ViewHolder {
		public LinearLayout menu_item;
		public ImageView goodsImage;
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
