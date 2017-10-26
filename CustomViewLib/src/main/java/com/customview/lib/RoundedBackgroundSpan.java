package com.customview.lib;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

public class RoundedBackgroundSpan extends ReplacementSpan{

	private int color, txtColor;
	public RoundedBackgroundSpan(int color, int txtColor) {
		super();
		this.color = color;
		this.txtColor = txtColor;
	}

	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end,
			FontMetricsInt fm) {
		return Math.round(measureText(paint, text, start, end));
	}

	private float measureText(Paint paint, CharSequence text, int start, int end){
		return paint.measureText(text, start, end)+5;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
			float x, int top, int y, int bottom, Paint paint) {
		// TODO Auto-generated method stub
		RectF rect = new RectF(x, top, x + measureText(paint, text, start, end), bottom);
		paint.setColor(color);
		canvas.drawRoundRect(rect, 5, 5, paint);
		paint.setColor(txtColor);
		canvas.drawText(text, start, end, x+2, y, paint);
	}

}
