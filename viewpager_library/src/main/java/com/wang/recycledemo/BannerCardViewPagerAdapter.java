package com.wang.recycledemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wang.recycledemo.widget.ListUtils;
import com.wang.recycledemo.widget.RecyclingPagerAdapter;

import java.util.List;


/**卡片式view适配器
 * @ClassName:com.wang.recycledemo
 * @author: Administrator 闫亚琴
 * @date: 2017/9/6 9:58
 */
public class BannerCardViewPagerAdapter extends RecyclingPagerAdapter {
    private Context context;
    private List<Banner_ViewPager_Bean> urlList;
    private int size;
    private boolean isInfiniteLoop;
    private BannerCardViewPager.ConfigItemListener configItemListener;
    private EventClick eventClick;

    public BannerCardViewPagerAdapter(Context context, List<Banner_ViewPager_Bean> urlList, BannerCardViewPager.ConfigItemListener configItemListener, EventClick eventClick) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_card_view_adapter, null);
            holder = new ViewHolder(view);
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
    public  class ViewHolder {

        ImageView imageView;
        public ViewHolder(View view){
            imageView = (ImageView)view.findViewById(R.id.imageView);
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
    public BannerCardViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

}
