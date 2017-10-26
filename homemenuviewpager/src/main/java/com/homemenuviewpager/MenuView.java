package com.homemenuviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MenuView extends LinearLayout {

    private Context context;

    public MenuView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    //	private MenuViewPager menuViewPager;
    private ViewPager menuViewPager;
    private RadioGroup dotMenuPagerGroupButtons;

    private void initView(Context context) {
        // View.inflate(context, R.layout.menu_layout, this);
        LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);
        inflater.inflate(R.layout.menu_layout, this);
        menuViewPager = (MenuViewPager) this.findViewById(R.id.menuViewPager);
        //		menuViewPager = (ViewPager) this.findViewById(R.id.menuViewPager);
        dotMenuPagerGroupButtons = (RadioGroup) this
                .findViewById(R.id.dotMenuPagerGroupButtons);
    }

    public ConfigItemListener configItemListener;

    public interface ConfigItemListener {
        public void configItemImage(ImageView imageView, String imageUrl);

        public void configItemText(TextView textView);
    }

    public void setOnConfigItemListener(ConfigItemListener configItemListener) {
        this.configItemListener = configItemListener;
    }

    private int viewPagerNum = 10;// viewPager每页最多显示个数

    public final int getViewPagerNum() {
        return viewPagerNum;
    }

    public final void setViewPagerNum(int viewPagerNum) {
        this.viewPagerNum = viewPagerNum;
    }

    int countViewPaget;//需要创建viewPager的页数
    private List<HomeMenuGridAdapter> adapters=new ArrayList<>();
    /**
     * 设置菜单栏
     *
     * @param menuList
     * @Title: setMenuViewPager
     * @Description: TODO
     * @return: void
     */
    public void setMenuViewPager(List<HomeMenuBean> menuList) {
        if (null != menuList && menuList.size() > 0) {
            if (menuViewPager.getChildCount() > 0) {
                menuViewPager.removeAllViews();
            }
            dotMenuPagerGroupButtons.removeAllViews();
            // ---------------------判断viewPager需要创建的页数-------------------------------------------------
            int countViewPaget = 0;// 需要创建viewPager的页数
                /* int viewPagerNum = 10;// viewPager每页最多显示个数 */
            if (menuList.size() / viewPagerNum == 0) {
                // menuList个数小于viewPagerNum，viewPager只加载一页
                countViewPaget = 1;
            } else if (menuList.size() / viewPagerNum > 0
                    && menuList.size() % viewPagerNum == 0) {
                // menuList个数大于viewPagerNum并且是viewPagerNum的倍数
                countViewPaget = menuList.size() / viewPagerNum;
            } else if (menuList.size() / viewPagerNum > 0
                    && menuList.size() % viewPagerNum > 0) {
                // menuList个数大于viewPagerNum并且不是viewPagerNum的倍数
                countViewPaget = menuList.size() / viewPagerNum + 1;
            }

            // --------------------menuViewPager创建---------------------------------------

            float density = context.getResources().getDisplayMetrics().density;// 屏幕密度
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
                    (int) (12 * density), (int) (20 * density));

            List<View> viewPagerList = new ArrayList<View>();// 创建存储HomeViewPageer数据

            // 循环创建GridViewForScrollView
            for (int i = 0; i < countViewPaget; i++) {
                NoScrollGridView gridViewForScrollView;
                List<HomeMenuBean> menuViewPagerList = new ArrayList<HomeMenuBean>();
                if (i < countViewPaget - 1) {
                    // 不是最后一页的时候
                    for (int k = i * viewPagerNum; k < i * viewPagerNum
                            + viewPagerNum; k++) {
                        menuViewPagerList.add(menuList.get(k));
                    }
                } else {
                    // 最后一页的时候
                    for (int k = i * viewPagerNum; k < menuList.size(); k++) {
                        menuViewPagerList.add(menuList.get(k));
                    }

                }
                gridViewForScrollView = initMenuGridView(menuViewPagerList);// 创建并初始化gridViewForScrollView
                viewPagerList.add(gridViewForScrollView);// 将gridViewForScrollView添加到viewPager

                // viewPager的页数如果只有一页，不加载滚动的豆豆
                if (countViewPaget <= 1)
                    break;

                // ---------------------------循环创建广告墙滚动的豆豆----------------------------

                // 动态循环创建RadioButton来展示循环的小圆点
                RadioButton dotButton = new RadioButton(context);
                dotButton.setId(i);// 设置id
                dotButton.setGravity(Gravity.CENTER_VERTICAL);
                if (i == 0) {
                    dotButton.setChecked(true);
                }
                dotButton.setButtonDrawable(radioButtonDrawable);
                dotButton.setTag(i);// 设置当前位置
                // 为点注册checked事件，当点击对应的点时，Viewpager切换到对应的page,并将点击的点设置为高亮

                dotMenuPagerGroupButtons.addView(dotButton, params_rb);
                // dotMenuPagerGroupButtons.check(0);// 将第一个小白点设置为高亮
            }
            HomeMenuViewPageerAdapter viewPageerAdapter = new HomeMenuViewPageerAdapter(
                    viewPagerList);
            menuViewPager.setAdapter(viewPageerAdapter);
            menuViewPager.setCurrentItem(0);// 设置viewPager默认加载页
            menuViewPager.setOffscreenPageLimit(countViewPaget);

            // 设置一个监听器，当ViewPager中的页面改变时调用
            menuViewPager.setOnPageChangeListener(new MenuPageChangeListener());
        }
    }

    int radioButtonDrawable = R.drawable.home_menu_pager_dot_bgs;//RadioButton循环的小圆点的颜色

    /**
     * 设置RadioButton循环的小圆点的颜色
     * @param radioButtonDrawable
     */
    public void setRadioButtonDrawable(int radioButtonDrawable) {
        this.radioButtonDrawable = radioButtonDrawable;
    }


    /**
     * 设置菜单栏
     *
     * @param menuList
     * @param viewPagerNum       每页最多显示几条
     * @param gridViewNumColumns gridView有几列
     * @Title: setMenuViewPager
     * @Description: TODO
     * @return: void
     */
    public void setMenuViewPager(List<HomeMenuBean> menuList, int viewPagerNum, int gridViewNumColumns) {
        this.viewPagerNum = viewPagerNum;
        this.gridViewNumColumns = gridViewNumColumns;
        if (null != menuList && menuList.size() > 0) {
            if (menuViewPager.getChildCount() > 0) {
                menuViewPager.removeAllViews();
            }
            dotMenuPagerGroupButtons.removeAllViews();
            // ---------------------判断viewPager需要创建的页数-------------------------------------------------
            countViewPaget = 0;// 需要创建viewPager的页数
			/* int viewPagerNum = 10;// viewPager每页最多显示个数 */
            if (menuList.size() / viewPagerNum == 0) {
                // menuList个数小于viewPagerNum，viewPager只加载一页
                countViewPaget = 1;
            } else if (menuList.size() / viewPagerNum > 0
                    && menuList.size() % viewPagerNum == 0) {
                // menuList个数大于viewPagerNum并且是viewPagerNum的倍数
                countViewPaget = menuList.size() / viewPagerNum;
            } else if (menuList.size() / viewPagerNum > 0
                    && menuList.size() % viewPagerNum > 0) {
                // menuList个数大于viewPagerNum并且不是viewPagerNum的倍数
                countViewPaget = menuList.size() / viewPagerNum + 1;
            }

            // --------------------menuViewPager创建---------------------------------------

            float density = context.getResources().getDisplayMetrics().density;// 屏幕密度
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
                    (int) (12 * density), (int) (20 * density));

            List<View> viewPagerList = new ArrayList<View>();// 创建存储HomeViewPageer数据

            // 循环创建GridViewForScrollView
            for (int i = 0; i < countViewPaget; i++) {
                NoScrollGridView gridViewForScrollView;
                List<HomeMenuBean> menuViewPagerList = new ArrayList<HomeMenuBean>();
                if (i < countViewPaget - 1) {
                    // 不是最后一页的时候
                    for (int k = i * viewPagerNum; k < i * viewPagerNum
                            + viewPagerNum; k++) {
                        menuViewPagerList.add(menuList.get(k));
                    }
                } else {
                    // 最后一页的时候
                    for (int k = i * viewPagerNum; k < menuList.size(); k++) {
                        menuViewPagerList.add(menuList.get(k));
                    }

                }
                gridViewForScrollView = initMenuGridView(menuViewPagerList);// 创建并初始化gridViewForScrollView
                viewPagerList.add(gridViewForScrollView);// 将gridViewForScrollView添加到viewPager

                // viewPager的页数如果只有一页，不加载滚动的豆豆
                if (countViewPaget <= 1)
                    break;

                // ---------------------------循环创建广告墙滚动的豆豆----------------------------

                // 动态循环创建RadioButton来展示循环的小圆点
                RadioButton dotButton = new RadioButton(context);
                dotButton.setId(i);// 设置id
                dotButton.setGravity(Gravity.CENTER_VERTICAL);
                if (i == 0) {
                    dotButton.setChecked(true);
                }
                dotButton.setButtonDrawable(radioButtonDrawable);
                dotButton.setTag(i);// 设置当前位置
                // 为点注册checked事件，当点击对应的点时，Viewpager切换到对应的page,并将点击的点设置为高亮

                dotMenuPagerGroupButtons.addView(dotButton, params_rb);
                // dotMenuPagerGroupButtons.check(0);// 将第一个小白点设置为高亮
            }
            HomeMenuViewPageerAdapter viewPageerAdapter = new HomeMenuViewPageerAdapter(
                    viewPagerList);
            menuViewPager.setAdapter(viewPageerAdapter);
            menuViewPager.setCurrentItem(0);// 设置viewPager默认加载页
            menuViewPager.setOffscreenPageLimit(countViewPaget);

            // 设置一个监听器，当ViewPager中的页面改变时调用
            menuViewPager.setOnPageChangeListener(new MenuPageChangeListener());
        }
    }

    /**
     * 更新adapter
     * @param menuList
     */
    public void updataAdapter(List<HomeMenuBean> menuList){
        //更新apapter
        for (int i = 0; i < countViewPaget; i++) {
            HomeMenuGridAdapter menuGridAdapter=adapters.get(i);
            if(i==0){
                //第一页
                if(menuList.size()>=viewPagerNum){
                    menuGridAdapter.setData(menuList.subList(0,viewPagerNum));
                }else {
                    menuGridAdapter.setData(menuList);
                }
            }else {
                if(menuList.size()>=(i+1)*viewPagerNum){
                    menuGridAdapter.setData(menuList.subList(i*viewPagerNum,(i+1)*viewPagerNum));
                }else {
                    menuGridAdapter.setData(menuList.subList(i*viewPagerNum,menuList.size()));
                }
            }
            menuGridAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author Administrator
     */
    private int currentItem = 0; // 当前图片的索引号

    private class MenuPageChangeListener implements OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {

            currentItem = position;
            // tv_title.setText(titles[position]);
            ((RadioButton) dotMenuPagerGroupButtons.getChildAt(oldPosition))
                    .setChecked(false);
            ((RadioButton) dotMenuPagerGroupButtons.getChildAt(position))
                    .setChecked(true);
            oldPosition = position;
        }

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

            if (arg0 == 1) {

            } else if (arg0 == 0) {

            }

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    private int gridViewNumColumns = 4;

    private int gridViewBackgroundColor=R.color.white;
    private int horizontalSpacing=0;
    private int verticalSpacing=0;

    /**
     * 设置GridView背景色
     * @param color
     */
    public void setGridViewBackgroundColor(int color){
        this.gridViewBackgroundColor=color;
    }

    /**
     * 设置GridView水平间距
     * @param horizontalSpacing
     */
    public void setGridViewHorizontalSpacing(int horizontalSpacing){
        this.horizontalSpacing=horizontalSpacing;
    }
    /**
     * 设置GridView垂直间距
     * @param verticalSpacing
     */
    public void setGridViewVerticalSpacing(int verticalSpacing){
        this.verticalSpacing=verticalSpacing;
    }

    public final int getGridViewNumColumns() {
        return gridViewNumColumns;
    }

    public final void setGridViewNumColumns(int gridViewNumColumns) {
        this.gridViewNumColumns = gridViewNumColumns;
    }

    private HomeMenuGridAdapter myGridViewAdapter;

    public HomeMenuGridAdapter getMyGridViewAdapter() {
        return myGridViewAdapter;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private NoScrollGridView initMenuGridView(List<HomeMenuBean> menuList) {

        NoScrollGridView gridViewForScrollView = new NoScrollGridView(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        gridViewForScrollView.setLayoutParams(params);
        gridViewForScrollView.setBackgroundColor(getResources().getColor(
                gridViewBackgroundColor));
        gridViewForScrollView.setDrawSelectorOnTop(false);
        gridViewForScrollView.setFastScrollAlwaysVisible(false);
        gridViewForScrollView.setGravity(Gravity.CENTER_VERTICAL);
        gridViewForScrollView.setVerticalScrollBarEnabled(false);// android:scrollbars="none"
        gridViewForScrollView.setNumColumns(gridViewNumColumns);// 设置grid的列数
        gridViewForScrollView
                .setStretchMode(NoScrollGridView.STRETCH_COLUMN_WIDTH);// 设置grid中的条目以什么缩放模式去填充空间。
        gridViewForScrollView.setHorizontalSpacing(horizontalSpacing);// 设置列间距
        gridViewForScrollView.setVerticalSpacing(verticalSpacing);// 设置行间距
        gridViewForScrollView.setFadingEdgeLength(1);// 设置边框渐变的长度
        gridViewForScrollView.setCacheColorHint(0);// 去除listview的拖动背景色
        gridViewForScrollView.setSelector(android.R.color.transparent);// android:listSelector="@null"设置透明来代替@null
        gridViewForScrollView.setOnItemClickListener(new menuItemListener(
                menuList));
        if (null != configItemListener) {
            myGridViewAdapter = new HomeMenuGridAdapter(context, configItemListener);
            myGridViewAdapter.setData(menuList);
            gridViewForScrollView.setAdapter(myGridViewAdapter);
            adapters.add(myGridViewAdapter);
        }
        return gridViewForScrollView;

    }

    private UrlSkipListener urlSkipListener;
    private UrlSkipListener2 urlSkipListener2;
    private UrlSkipListener3 urlSkipListener3;

    public interface UrlSkipListener {
        public void UrlSkip(String... urlInfo);
    }
    public interface UrlSkipListener2 {
        public void UrlSkip(int position,String... urlInfo);
    }
    public interface UrlSkipListener3 {
        public void UrlSkip(int allPosition,String... urlInfo);
    }
    public void setOnUrlSkipListener2(UrlSkipListener2 urlSkipListener) {
        this.urlSkipListener2 = urlSkipListener;
    }
    public void setOnUrlSkipListener(UrlSkipListener urlSkipListener) {
        this.urlSkipListener = urlSkipListener;
    }
    public void setOnUrlSkipListener3(UrlSkipListener3 urlSkipListener) {
        this.urlSkipListener3 = urlSkipListener;
    }

    /**
     * 菜单按钮的OnItemClickListener
     */
    private class menuItemListener implements OnItemClickListener {
        List<HomeMenuBean> menu_list;

        menuItemListener(List<HomeMenuBean> menu_list) {
            this.menu_list = menu_list;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            HomeMenuBean menuBean = menu_list.get(position);
            try {
                String menuUrl = menuBean.getMenu_url();
                if (null != urlSkipListener) {
                    urlSkipListener.UrlSkip(menuUrl, menuBean.getMenu_title());
                }else if(null !=urlSkipListener2){
                    urlSkipListener2.UrlSkip(position,menuUrl, menuBean.getMenu_title());
                }else if(null !=urlSkipListener3){
                    urlSkipListener3.UrlSkip((currentItem*viewPagerNum)+position,menuUrl, menuBean.getMenu_title());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
