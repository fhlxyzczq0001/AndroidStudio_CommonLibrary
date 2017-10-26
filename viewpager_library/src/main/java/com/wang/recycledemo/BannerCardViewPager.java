package com.wang.recycledemo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.recycledemo.widget.AutoScrollViewPager;
import com.wang.recycledemo.widget.DisplayUtil;
import com.wang.recycledemo.widget.ListUtils;

import java.util.List;

/**卡片式view
 * @ClassName:
 * @author: Administrator 闫亚琴
 * @date: 2017/9/6 11:49
 */
public class BannerCardViewPager extends LinearLayout {

    private Context context;
    //轮播图父布局
    RelativeLayout home_banner_layout;
    //轮播图控件
    AutoScrollViewPager home_banner_pager;
    //轮播图title
    TextView home_banner_tag_title;
    //轮播图当前位置标识布局
    LinearLayout home_banner_tag_layout;
    private int oldPosition = 0, index = 0;// 记录ScrollView滚动位置
    BannerTabListener mBannerTabListener;
    private RelativeLayout bottom_lay;
    boolean isInfiniteLoop=true;

    public List<Banner_ViewPager_Bean> getmViewList() {
        return mViewList;
    }

    private List<Banner_ViewPager_Bean> mViewList;

    /**
     * 位置监听回调
     */
    public interface BannerTabListener  {
        public void onBannerTabListener(int index,int count);
    }

    public BannerCardViewPager(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public BannerCardViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public BannerCardViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);
        inflater.inflate(R.layout.banner_cardviewpager_layout, this);
        home_banner_layout=(RelativeLayout)this.findViewById(R.id.home_banner_layout);
        home_banner_pager=(AutoScrollViewPager)this.findViewById(R.id.home_banner_pager);
        home_banner_tag_title=(TextView)this.findViewById(R.id.home_banner_tag_title);
        home_banner_tag_layout=(LinearLayout)this.findViewById(R.id.home_banner_tag_layout);
        bottom_lay = (RelativeLayout) this.findViewById(R.id.bottom_lay);
    }
    public void setBottom_layVisibility(int visibility){
        bottom_lay.setVisibility(visibility);
    }

    /**
     * 设置轮播图位置监听
     * @param bannerTabListener
     */
    public void setBannerTabListener(BannerTabListener bannerTabListener){
        this.mBannerTabListener=bannerTabListener;
    }

    /**
     * 设置轮播图
     * @param mViewList
     */
    public void setBannerCardViewPager(final List<Banner_ViewPager_Bean> mViewList){
        if (null == mViewList || mViewList.size() == 0) {
            return;
        }
        this.mViewList = mViewList;
        BannerCardViewPagerAdapter bannerPagerAdapter=new BannerCardViewPagerAdapter(context, mViewList, configItemListener, new EventClick() {
            @Override
            public void eventClick() {
                Banner_ViewPager_Bean viewPagerBean=mViewList.get(index);
                try {
                    String menuUrl = viewPagerBean.getGoods_detail();
                    if (null != urlSkipListener) {
                        urlSkipListener.UrlSkip(menuUrl, viewPagerBean.getGoods_title());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bannerPagerAdapter.setInfiniteLoop(isInfiniteLoop);// 设置是否轮播
        home_banner_pager.setAdapter(bannerPagerAdapter);

        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
        home_banner_pager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        home_banner_layout.setClipChildren(false);
        home_banner_pager.setOnPageChangeListener(new MyOnPageChangeListener(mViewList));
        home_banner_pager.setInterval(5000);// 轮播时间
        home_banner_pager.startAutoScroll();// 启动轮播
        home_banner_pager.setCurrentItem(index);// 初始化位置
        home_banner_pager.setOffscreenPageLimit(3);
        int pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 4.0f);
        ViewGroup.LayoutParams lp = home_banner_pager.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
        }
        //设置每页之间的左右间隔
        home_banner_pager.setPageMargin(10);
        home_banner_pager.setLayoutParams(lp);
        home_banner_tag_title.setText(mViewList.get(index).getGoods_title());// 设置标题
        //设置ViewPager切换效果，即实现画廊效果
        home_banner_pager.setPageTransformer(true, new MyGallyPageTransformer());
        initDots(mViewList.size());// 设置滚动豆豆
    }
    public AutoScrollViewPager getAutoScrollViewPager(){
        if(home_banner_pager!=null){
            return home_banner_pager;
        }
        return null;
    }

    public void stopAutoScroll(){
        home_banner_pager.stopAutoScroll();
    }

    int icon_tab_iv1=R.mipmap.icon_tab_iv1;
    int icon_tab_iv0=R.mipmap.icon_tab_iv0;

    /**
     * 设置轮播图滚动豆豆
     * @param icon_tab_iv1
     * @param icon_tab_iv0
     */
    public void setDots(int icon_tab_iv1,int icon_tab_iv0){
        this. icon_tab_iv1=icon_tab_iv1;
        this. icon_tab_iv0=icon_tab_iv0;
    };

    /**
     * 初始化轮播图滚动豆豆
     *
     * @Title: initDots
     * @Description: TODO
     * @return: void
     */
    public void initDots(int size) {
        home_banner_tag_layout.removeAllViews();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                home_banner_tag_layout.addView(setDaoHangText(icon_tab_iv1));
            } else {
                home_banner_tag_layout.addView(setDaoHangText(icon_tab_iv0));
            }
        }
    }

    /**
     * 设置豆豆布局
     *
     * @param id
     * @return
     * @Title: setDaoHangText
     * @Description: TODO
     * @return: View
     */
    private View setDaoHangText(int id) {
        View pointer = new View(context);
        LayoutParams Viewpar = new LayoutParams(
                DisplayUtil.dip2px(context, 8), DisplayUtil.dip2px(context, 8));
        Viewpar.setMargins(5, 2, 5, 2);
        pointer.setLayoutParams(Viewpar);
        pointer.setBackgroundResource(id);

        return pointer;
    }

    /**
     * 轮播图滚动事件监听
     *
     * @ClassName: MyOnPageChangeListener
     * @Description: TODO
     * @author: Administrator杨重诚
     * @date: 2016-5-30 下午4:39:54
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        List<Banner_ViewPager_Bean> tab_ViewPager_Beans;
        public  MyOnPageChangeListener(List<Banner_ViewPager_Bean> tab_ViewPager_Beans){
            this.tab_ViewPager_Beans=tab_ViewPager_Beans;
        }
        @Override
        public void onPageSelected(int position) {
            index = (position) % ListUtils.getSize(tab_ViewPager_Beans);
            home_banner_tag_title.setText(tab_ViewPager_Beans.get(index).getGoods_title());
            if(home_banner_tag_layout.getChildCount()<=0){
                return;
            }
            home_banner_tag_layout.getChildAt(oldPosition).setBackgroundResource(
                    icon_tab_iv0);
            home_banner_tag_layout.getChildAt(
                    (position) % ListUtils.getSize(tab_ViewPager_Beans))
                    .setBackgroundResource(icon_tab_iv1);
            oldPosition = (position) % ListUtils.getSize(tab_ViewPager_Beans);

            if(mBannerTabListener!=null){
                mBannerTabListener.onBannerTabListener(index,home_banner_tag_layout.getChildCount());
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

			/*
             * OnPageChangeListener这个接口需要实现三个方法：（onPageScrollStateChanged，
			 * onPageScrolled ，onPageSelected）
			 *
			 * onPageScrollStateChanged(int arg0) ，此方法是在状态改变的时候调用，其中arg0这个参数
			 *
			 * 有三种状态（0，1，2）。arg0
			 * ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
			 */

            /*if (arg0 == 1) {

                //滑动时禁止下拉刷新
                customScrollView.setEnabled(false);
                scrollView.setEnabled(false);
            } else if (arg0 == 0) {

                //滑动时禁止下拉刷新
                customScrollView.setEnabled(true);
                scrollView.setEnabled(true);
            }*/
        }
    }

    /**
     * 设置banner图的高度
     * @param layoutParams
     */
    public void setBannerLayoutParams(LayoutParams layoutParams){
        home_banner_layout.setLayoutParams(layoutParams);
    }

    /**
     * 设置图片和文字的初始化接口
     */
    public ConfigItemListener configItemListener;

    public interface ConfigItemListener {
        public void configItemImage(ImageView imageView, String imageUrl);
    }

    public void setOnConfigItemListener(ConfigItemListener configItemListener) {
        this.configItemListener = configItemListener;
    }

    /**
     * 设置点击事件回调
     */
    private UrlSkipListener urlSkipListener;

    public interface UrlSkipListener {
        public void UrlSkip(String... urlInfo);
    }

    public void setOnUrlSkipListener(UrlSkipListener urlSkipListener) {
        this.urlSkipListener = urlSkipListener;
    }

    public LinearLayout getBannerTagLayout(){
        return home_banner_tag_layout;
    }

    /**
     * 设置是否显示标题
     * @param visibility
     */
    public void setBannerTagTitleVisibility(int visibility){
        home_banner_tag_title.setVisibility(visibility);
    }
    /**
     * 设置是否显示Dots
     * @param visibility
     */
    public void setBannerDotsVisibility(int visibility){
        home_banner_tag_layout.setVisibility(visibility);
    }

    /**
     * 设置是否轮播
     * @param isInfiniteLoop
     */
    public void setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
    }

    /**
     * 设置轮播图位置
     * @param index
     */
    public void setCurrentItem(int index){
        this.index=index;
    }
}
