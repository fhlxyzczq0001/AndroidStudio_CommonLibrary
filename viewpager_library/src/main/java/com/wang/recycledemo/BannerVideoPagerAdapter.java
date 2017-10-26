package com.wang.recycledemo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.videomodel.VideoPlayer;
import com.wang.recycledemo.widget.ListUtils;
import com.wang.recycledemo.widget.RecyclingPagerAdapter;

import java.util.List;

import static com.wang.recycledemo.R.id.videoView;

/**
 * 首页轮播图适配器
 * @ClassName: MainIntroFragment_BannerPagerAdapter
 * @Description: TODO
 * @author: Administrator杨重诚
 * @date: 2016-5-30 下午4:36:55
 */
	public class BannerVideoPagerAdapter extends RecyclingPagerAdapter {
		private Context context;
	    private List<Banner_ViewPager_Bean> urlList;
	    private int size;
	    private boolean isInfiniteLoop;
	    private BannerViewPagers.ConfigItemListener configItemListener;
	    private EventClick eventClick;
		ViewHolder holder = null;
		public BannerVideoPagerAdapter(Context context, List<Banner_ViewPager_Bean> urlList,boolean isInfiniteLoop, BannerViewPagers.ConfigItemListener configItemListener, EventClick eventClick) {
			this.context = context;
			this.urlList = urlList;
			this.size = ListUtils.getSize(urlList);
			this.isInfiniteLoop = isInfiniteLoop;
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
//			final ViewHolder holder ;
	        if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(R.layout.item_video_view_adapter, null);

				holder.imageView = (ImageView) view.findViewById(R.id.imageView);
				holder.imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						eventClick.eventClick();
					}
				});
				holder.videoView_lay = (FrameLayout) view.findViewById(R.id.videoView_lay);
				holder.videoView = (VideoPlayer) view.findViewById(videoView);
				holder.videoStartIcon = (ImageView)view.findViewById(R.id.videoStartIcon);

				view.setTag(holder);
	        } else {
	            holder = (ViewHolder) view.getTag();
	        }
			//首先判断链接地址是否是视频后缀
			//if(true){ videoview中设置视频地址，再设置图片资源，设置视频资源，设置点击图片开始播放视频}
			// else{ 直接在imageview设置图片显示 }
			holder.videoStartIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.e("1111", "点击视频播放按钮");
					holder.videoStartIcon.setVisibility(View.GONE);
					holder.videoView.setVisibility(View.VISIBLE);
					String videoUrl = urlList.get(getPosition(position)).getGoods_detail();
					String videoTitle = urlList.get(getPosition(position)).getGoods_title();
					holder.videoView.playVideo(videoUrl, videoTitle);
				}
			});

			if (null != configItemListener) {
				String videoUrl = urlList.get(getPosition(position)).getGoods_detail();
				String suffix = videoUrl.substring(videoUrl.lastIndexOf("."));
				if (suffix.equals(".mp4")){
					//需要加载视频
					holder.imageView.setVisibility(View.VISIBLE);
					holder.videoView_lay.setVisibility(View.VISIBLE);
					holder.videoView.setVisibility(View.GONE);

					holder.videoStartIcon.setVisibility(View.VISIBLE);
					this.configItemListener.configItemImage(holder.imageView, urlList.get(getPosition(position)).getGoods_image());
				}else {
					//都是图片
					holder.videoView_lay.setVisibility(View.GONE);
					holder.imageView.setVisibility(View.VISIBLE);
					this.configItemListener.configItemImage(holder.imageView, urlList.get(getPosition(position)).getGoods_image());
				}
			}
	        return view;
	    }

	    private static class ViewHolder {
	        ImageView imageView;//图片布局
			FrameLayout videoView_lay;//视频布局
			VideoPlayer videoView;//加载视频控件
			ImageView videoStartIcon;//视频开始按钮
	    }


	/**
	 * 销毁videoview
	 */
	public void setVideoViewDestory(){
		if(holder.videoView != null){
			Log.e("111111", "销毁方法");
			holder.videoView.destroyVideo();
			holder.videoView = null;
		}
	}

	public void setVideoViewFullScreen(){
		Log.e("111111", "调用关闭全屏方法");
			if (holder.videoView.isFullScreen()){
				holder.videoView.setProtrait();
				return;
			}
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
	    public BannerVideoPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
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
