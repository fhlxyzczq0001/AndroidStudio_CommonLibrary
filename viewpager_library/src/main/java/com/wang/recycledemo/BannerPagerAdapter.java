package com.wang.recycledemo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.wang.recycledemo.widget.ListUtils;
import com.wang.recycledemo.widget.RecyclingPagerAdapter;

import java.util.List;

/**
 * 首页轮播图适配器
 * @ClassName: MainIntroFragment_BannerPagerAdapter
 * @Description: TODO
 * @author: Administrator杨重诚
 * @date: 2016-5-30 下午4:36:55
 */
	public class BannerPagerAdapter extends RecyclingPagerAdapter {
		private Context context;
	    private List<Banner_ViewPager_Bean> urlList;
	    private int size;
	    private boolean isInfiniteLoop;
	    private BannerViewPagers.ConfigItemListener configItemListener;
	    private EventClick eventClick;
	    public BannerPagerAdapter(Context context, List<Banner_ViewPager_Bean> urlList, BannerViewPagers.ConfigItemListener configItemListener, EventClick eventClick) {
	    	this.context = context;
	        this.urlList = urlList;
	        this.size = ListUtils.getSize(urlList);
	        isInfiniteLoop = true;
			this.configItemListener = configItemListener;
			this.eventClick=eventClick;
	    }

	    @Override
	    public int getCount() {
	        // Infinite loop
	        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(urlList);
	    }

	    /**
	     * get really position
	     * 
	     * @param position
	     * @return
	     */
	    private int getPosition(int position) {
	        return isInfiniteLoop ? position % size : position;
	    }

	    @Override
	    public View getView(final int position, View view, ViewGroup container) {
	        ViewHolder holder;
	        if (view == null) {
	            holder = new ViewHolder();
	            view = holder.imageView = getView(context);
				holder.imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						eventClick.eventClick();
					}
				});
	            view.setTag(holder);
	        } else {
	            holder = (ViewHolder) view.getTag();
	        }
			if (null != configItemListener) {
				this.configItemListener.configItemImage(holder.imageView, urlList.get(getPosition(position)).getGoods_image());
			}
	        return view;
	    }

	    private static class ViewHolder {

	        ImageView imageView;
	    }

	    /**
	     * @return the isInfiniteLoop
	     */
	    public boolean isInfiniteLoop() {
	        return isInfiniteLoop;
	    }

	    /**
	     * @param isInfiniteLoop
	     *            the isInfiniteLoop to set
	     */
	    public BannerPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
	        this.isInfiniteLoop = isInfiniteLoop;
	        return this;
	    }
	    
	    private ImageView getView(Context context) {
			ImageView imageView = new ImageView(context);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.FIT_XY);
			if(!isInfiniteLoop){
				imageView.setBackgroundColor(Color.parseColor("#ffffff"));
				imageView.setPadding(10,10,10,10);
			}
			return imageView;
		}
	}
