package com.jazzy.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 */
public class GuideViewPagers extends LinearLayout implements ViewPager.OnPageChangeListener {
    private Context context;
    // 定义ViewPager对象
    private JazzyViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 引导图片资源
    private int[] guidePicIds;
    // 底部小点的图片
    private ImageView[] points;
    // 记录当前选中位置
    private int currentIndex = 0;
    //Button okBut;
    TextView loging_bt, tiyan_bt;//登录 /体验
    private Animation animationTop;//渐变放大效果
    LinearLayout guideActivity_linearLayout;//底部小圆点

    public GuideViewPagers(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public GuideViewPagers(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public GuideViewPagers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
        inflater.inflate(R.layout.activity_guide, this);
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 实例化ViewPager
        viewPager = (JazzyViewPager) findViewById(R.id.guideActivity_viewpager);

        loging_bt = (TextView) findViewById(R.id.loging_bt);
        tiyan_bt = (TextView) findViewById(R.id.tiyan_bt);

        animationTop = AnimationUtils.loadAnimation(context,
                R.anim.tutorail_scalate_top);//渐变放大效果
    }

    public void setGuideData(int[] guidePicIds,int[] dot,int viewPagerBg){
        viewPager.setBackgroundResource(viewPagerBg);
        setDefaultGuidPage(guidePicIds);//设置默认引导页
        dot1=dot[0];
        dot2=dot[1];
        initData();
    }

    public void setLogingBtSty(int bg){
        loging_bt.setBackgroundResource(bg);
    }

    public void setTiYangBtSty(int bg){
        tiyan_bt.setBackgroundResource(bg);
    }

    public TextView getLogingBt() {
        return loging_bt;
    }

    public TextView getTiyanBt() {
        return tiyan_bt;
    }

    /**
     * 设置默认引导页
     *
     * @Title: setDefaultGuidPage
     * @Description: TODO
     * @return: void
     */
    private void setDefaultGuidPage(int[] guidePicIds) {
        this.guidePicIds = guidePicIds;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < guidePicIds.length; i++) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(mParams);
            //防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片资源
            iv.setBackgroundResource(guidePicIds[i]);
            views.add(iv);
        }
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views, viewPager);
        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);
        vpAdapter.notifyDataSetChanged();

        viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Stack);
        viewPager.setPageMargin(30);

        // 初始化底部小点
        initPoint();
    }
int dot1,dot2;
    /**
     * 初始化底部小点
     */
    private void initPoint() {
        guideActivity_linearLayout = (LinearLayout) findViewById(R.id.guideActivity_linearLayout);
        /*points = new ImageView[pics.length];*/
        points = new ImageView[guidePicIds.length];

        ImageView imgView;
        // 循环取得小点图片
        /*for (int i = 0; i < pics.length; i++) {*/
        for (int i = 0; i < guidePicIds.length; i++) {
            imgView = new ImageView(context);
            // 得到一个LinearLayout下面的每一个子元素
            points[i] = imgView;
            // 默认都设为灰色
            points[i].setImageResource(dot1);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            points[i].setLayoutParams(layoutParams);
            guideActivity_linearLayout.addView(imgView);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setImageResource(dot2);
    }

    /**
     * 滑动状态改变时调用
     */
    boolean flag = false;

    @Override
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                flag = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                flag = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (viewPager.getCurrentItem() == viewPager.getAdapter()
                        .getCount() - 1 && !flag) {
                    //最后一页
                }
                flag = true;
                break;
        }
    }

    /**
     * 当前页面滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int arg0) {
        if (arg0 == viewPager.getAdapter().getCount() - 1) {
            loging_bt.startAnimation(animationTop);
            tiyan_bt.startAnimation(animationTop);

            loging_bt.setVisibility(View.VISIBLE);
            tiyan_bt.setVisibility(View.VISIBLE);

            guideActivity_linearLayout.setVisibility(View.GONE);
        } else {

            guideActivity_linearLayout.setVisibility(View.VISIBLE);

            loging_bt.setVisibility(View.GONE);
            tiyan_bt.setVisibility(View.GONE);

            loging_bt.clearAnimation();
            tiyan_bt.clearAnimation();
        }
        viewPager.setBackgroundResource(guidePicIds[arg0]);//动态设置viewPager的背景图
        setCurDot(arg0);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        /*if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {*/
        if (positon < 0 || positon > guidePicIds.length - 1 || currentIndex == positon) {
            return;
        }
        points[currentIndex].setImageResource(dot1);
        points[positon].setImageResource(dot2);
        currentIndex = positon;
    }
    public void setLoging_btContent(String str){
        loging_bt.setText(str);
    }
}
