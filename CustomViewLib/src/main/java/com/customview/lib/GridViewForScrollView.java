package com.customview.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
public class GridViewForScrollView extends GridView {
	public GridViewForScrollView(Context context) {    
		super(context);    
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
	}    

	public GridViewForScrollView(Context context, AttributeSet attrs) {    
		super(context, attrs);   
		this.setFocusableInTouchMode(false);
	}    

	@Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {    
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);    
		super.onMeasure(widthMeasureSpec, expandSpec);    
	}

}
