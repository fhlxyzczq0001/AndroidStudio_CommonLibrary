package com.utlis.lib.pop;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.utlis.lib.R;
import com.utlis.lib.WindowUtils;

import java.util.List;

public class PopListWin {


	private Activity ctx;
	private View view;
	private PopupWindowUtils popWin;
//	private TextView tvNew, tvPhoto, tvCancle;
	private ListView listView;
	private OnSelectClickListener onSelectClickListener;

	/**
	 * 构造
	 *
	 * @param ctx
	 */
	public PopListWin(Activity ctx) {
		this.ctx = ctx;
		initView();
		if (popWin == null)
			popWin = new PopupWindowUtils(ctx).setContentView(view);
	}

	/**
	 * 显示
	 * 
	 * @param parent
	 */
	public void show(View parent) {
		popWin.show(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}


	private void initView() {
		view = View.inflate(ctx, R.layout.pop_menu_other, null);
		listView = (ListView) view.findViewById(R.id.popMenuOther_listview);
	}

	/**
	 * 设置数据源
	 * @param data
     */
	public void setListData(final List<String> data){
		listView.setAdapter(new ArrayAdapter<String>
				(ctx, R.layout.pop_menu_other_item,data));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				popWin.dismiss();
				if(onSelectClickListener == null){
					throw new NullPointerException("onSelectClickListener is null");
				}
				if(data.size()-1>=position){
					onSelectClickListener.onSelectClickListener(true, ""+data.get(position), position);
				}else{
					onSelectClickListener.onSelectClickListener(false, "", -1);
				}
			}
		});
		int scrheight = WindowUtils.getWindowHeight(ctx)  / 3;
		if(data.size() * 45 > scrheight){
			popWin.setHeight(scrheight);
		}
	}

	/**
	 * 设置监听回调
	 * @param onSelectClickListener
     */
	public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
		this.onSelectClickListener = onSelectClickListener;
	}

	/**
	 * 选择对话框的回调方法
	 */
	public interface OnSelectClickListener{
		/**
		 * 选择对话框的回调方法
		 * @param b
		 * @param str
         * @param index
         */
		public void onSelectClickListener(boolean b, String str, int index);
	}
}
