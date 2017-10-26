package com.example.homemenuviewpager;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 菜单布局的封装，默认一页10个，四列，图片占屏幕宽度0.1 需要的参数 
 * 1.viewPagerNum 一页显示的总数
 * 2.gridViewNumColumns 列数 
 * 3.setOnConfigItemListener设置每个条目的图片路径以及名字,
 * 如果给bean对象设置过menu_icon（菜单图标的url）则不需要设置menu_icon_drawable
 * 如果设置过menu_icon_drawable（菜单图标本地id），则不需要设置menu_icon
 * 4.setOnUrlSkipListener设置条目点击跳转链接监听 
 * 5.setMenuViewPager 使用该方法初始化数据
 */
public class MenuView extends LinearLayout {

	private Context context;

	public MenuView(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	@SuppressLint("NewApi")
	public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initView(context);
	}

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);
	}

	private MenuViewPager menuViewPager;
	private LinearLayout dotLay;
	private RadioGroup dotMenuPagerGroupButtons;
	private boolean isShowSpacing;
	// 图片大小比率 占屏幕多少
	private double imgWidthRate = 0.1;
	// 图片大小比率 占屏幕多少
	private double topImgWidthRate = 0.4;

	public final double getImgWidthRate() {
		return imgWidthRate;
	}

	public final void setImgWidthRate(double imgWidthRate) {
		this.imgWidthRate = imgWidthRate;
	}

	private void initView(Context context) {
		// View.inflate(context, R.layout.menu_layout, this);
		LayoutInflater.from(context).inflate(R.layout.menu_layout, this, true);
		menuViewPager = (MenuViewPager) this.findViewById(R.id.menuViewPager);
		dotLay = (LinearLayout) this.findViewById(R.id.dotLay);
		dotMenuPagerGroupButtons = (RadioGroup) this
				.findViewById(R.id.dotMenuPagerGroupButtons);
	}

	private ConfigItemListener configItemListener;

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

	private int gridViewNumColumns = 4;

	public final int getGridViewNumColumns() {
		return gridViewNumColumns;
	}

	public final void setGridViewNumColumns(int gridViewNumColumns) {
		this.gridViewNumColumns = gridViewNumColumns;
	}


	public void setMenuViewPager(List<HomeMenuBean> menuList) {
		setMenuViewPager(menuList, viewPagerNum, gridViewNumColumns,
				imgWidthRate, true, false,topImgWidthRate);
	}

	public void setMenuViewPager(List<HomeMenuBean> menuList, int viewPagerNum,
			int gridViewNumColumns) {
		setMenuViewPager(menuList, viewPagerNum, gridViewNumColumns,
				imgWidthRate, true, false,topImgWidthRate);
	}

	public void setMenuViewPager(List<HomeMenuBean> menuList, int viewPagerNum,
			int gridViewNumColumns, double imgWidthRate) {
		setMenuViewPager(menuList, viewPagerNum, gridViewNumColumns,
				imgWidthRate, true, false,topImgWidthRate);
	}

	public void setMenuViewPager(List<HomeMenuBean> menuList, int viewPagerNum,
			int gridViewNumColumns, double imgWidthRate, boolean isShowDot) {
		setMenuViewPager(menuList, viewPagerNum, gridViewNumColumns,
				imgWidthRate, isShowDot, false,topImgWidthRate);
	}
	public void setMenuViewPager(List<HomeMenuBean> menuList, int viewPagerNum,
			int gridViewNumColumns, double imgWidthRate, boolean isShowDot,boolean isShowSpacing) {
		setMenuViewPager(menuList, viewPagerNum, gridViewNumColumns,
				imgWidthRate, isShowDot, isShowSpacing,topImgWidthRate);
	}


	public void setMenuViewPager(List<HomeMenuBean> menuList, int viewPagerNum,
			int gridViewNumColumns, double imgWidthRate, boolean isShowDot,
			boolean isShowSpacing, double topImgWidthRate) {
		this.viewPagerNum = viewPagerNum;
		this.gridViewNumColumns = gridViewNumColumns;
		this.isShowSpacing = isShowSpacing;
		this.topImgWidthRate = topImgWidthRate;
		if (imgWidthRate > 0 && imgWidthRate <= 1) {
			this.imgWidthRate = imgWidthRate;
		} else {
			this.imgWidthRate = 0.1;
		}
		if (!isShowDot) {
			dotLay.setVisibility(View.GONE);
		} else {
			dotLay.setVisibility(View.VISIBLE);
		}
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
				if (isShowDot) {
					// 动态循环创建RadioButton来展示循环的小圆点
					RadioButton dotButton = new RadioButton(context);
					dotButton.setId(i);// 设置id
					dotButton.setGravity(Gravity.CENTER_VERTICAL);
					if (i == 0) {
						dotButton.setChecked(true);
					}
					dotButton
							.setButtonDrawable(R.drawable.home_menu_pager_dot_bgs);
					dotButton.setTag(i);// 设置当前位置
					// 为点注册checked事件，当点击对应的点时，Viewpager切换到对应的page,并将点击的点设置为高亮

					dotMenuPagerGroupButtons.addView(dotButton, params_rb);
					// dotMenuPagerGroupButtons.check(0);// 将第一个小白点设置为高亮
				}
			}
			HomeMenuViewPageerAdapter viewPageerAdapter = new HomeMenuViewPageerAdapter(
					viewPagerList);
			menuViewPager.setAdapter(viewPageerAdapter);
			menuViewPager.setCurrentItem(0);// 设置viewPager默认加载页
			menuViewPager.setOffscreenPageLimit(countViewPaget);

			// 设置一个监听器，当ViewPager中的页面改变时调用
			menuViewPager.setOnPageChangeListener(new MenuPageChangeListener(
					isShowDot));
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private int currentItem = 0; // 当前图片的索引号

	private class MenuPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;
		boolean isShowDot;

		public MenuPageChangeListener(boolean isShowDot) {
			this.isShowDot = isShowDot;
		}

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {

			currentItem = position;
			// tv_title.setText(titles[position]);
			if (isShowDot) {
				((RadioButton) dotMenuPagerGroupButtons.getChildAt(oldPosition))
						.setChecked(false);
				((RadioButton) dotMenuPagerGroupButtons.getChildAt(position))
						.setChecked(true);
			}
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

	private HomeMenuGridAdapter myGridViewAdapter;

	public final HomeMenuGridAdapter getMyGridViewAdapter() {
		return myGridViewAdapter;
	}

	public final void setMyGridViewAdapter(HomeMenuGridAdapter myGridViewAdapter) {
		this.myGridViewAdapter = myGridViewAdapter;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private NoScrollGridView initMenuGridView(List<HomeMenuBean> menuList) {
		NoScrollGridView gridViewForScrollView = new NoScrollGridView(context);
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		gridViewForScrollView.setLayoutParams(params);
		gridViewForScrollView.setBackgroundColor(getResources().getColor(
				R.color.white));
		gridViewForScrollView.setDrawSelectorOnTop(false);
		gridViewForScrollView.setFastScrollAlwaysVisible(false);
		gridViewForScrollView.setVerticalScrollBarEnabled(false);// android:scrollbars="none"
		gridViewForScrollView.setNumColumns(gridViewNumColumns);// 设置grid的列数
		gridViewForScrollView
				.setStretchMode(NoScrollGridView.STRETCH_COLUMN_WIDTH);// 设置grid中的条目以什么缩放模式去填充空间。
		gridViewForScrollView.setHorizontalSpacing(0);// 设置列间距
		gridViewForScrollView.setVerticalSpacing(0);// 设置行间距
		gridViewForScrollView.setFadingEdgeLength(0);// 设置边框渐变的长度
		gridViewForScrollView.setCacheColorHint(0);// 去除listview的拖动背景色
		gridViewForScrollView.setSelector(android.R.color.transparent);// android:listSelector="@null"设置透明来代替@null
		gridViewForScrollView.setOnItemClickListener(new menuItemListener(
				menuList));
		if (null != configItemListener) {
			myGridViewAdapter = new HomeMenuGridAdapter(context,
					gridViewNumColumns, isShowSpacing, imgWidthRate,
					configItemListener,topImgWidthRate);
		} else {
			myGridViewAdapter = new HomeMenuGridAdapter(context,
					gridViewNumColumns, isShowSpacing, imgWidthRate,topImgWidthRate);
		}
		myGridViewAdapter.setData(menuList);
		gridViewForScrollView.setAdapter(myGridViewAdapter);
		return gridViewForScrollView;

	}

	private UrlSkipListener urlSkipListener;
	
	public interface UrlSkipListener {
		public void UrlSkip(int position, String... urlInfo);
	}
	
	public void setOnUrlSkipListener(UrlSkipListener urlSkipListener) {
		this.urlSkipListener = urlSkipListener;
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
					urlSkipListener.UrlSkip(position, menuUrl,
							menuBean.getMenu_title());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
