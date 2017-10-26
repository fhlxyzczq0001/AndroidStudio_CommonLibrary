package windy.chzh.seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.SeekBar;

import java.math.BigDecimal;

/**
 * 刻度条
 * @Author: 晁政
 * @CreatTime: 2017/3/29 11:30 
 */


public class UiSeeKBar extends SeekBar {

    // 进度条指示文字后缀
    private String numTextFormat = "%";

    private String numText;
    // 进度条指示文字的大小吗默认20px
    private int numTextSize = 20;
    // 进度条指示文字的背景
    private int numbackground;
    // numbackground对应的bitmap
    private int numTextColor;
    private Bitmap bm;
    // bitmap对应的宽高
    private float bmp_width, bmp_height;
    // 构建画笔和文字
    Paint bmPaint;
    // 文本的宽可能不准
    private float numTextWidth;
    // 测量seekbar的规格
    private Rect rect_seek;
    // 测量thum的规格
    private Rect rect_thum;

    // show 在top还是bottom
    private int type = Gravity.TOP;
    private Paint.FontMetrics fm;
    // 特别说明这个scale比例是滑动的指示器小箭头部分占全部图片的比列，为了使其文字完全居中
    private double numScale = 0.16;
    // 是否显示首尾刻度线
    private boolean isShowStartEndMarkLine = false;
    // 是否显示首尾刻度线
    private int markLineColor;
    // 前缀
    private String prefix_text = "";
    // 是否显示文本背景图片
    private boolean isShowTxtBg = true;
    // 首尾刻度高度
    private int markLineHight;
    // 每个进度对应的单位
    private double unit = 1;
    private double start = 0;
    private double end = 100;
    private boolean isShowStartAndEnd = false;
    private int startAndEndColor;
    //是否需要四舍五入
    private boolean isRound = false;
    //小数点后几位小数
    private int digit = 0;

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    private double num;

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
        // invalidate();
        postInvalidate();
    }

    public UiSeeKBar(Context context) {
        this(context, null);
    }

    public UiSeeKBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UiSeeKBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化属性
        init(context, attrs);
        // 初始化bm
        initBm();
        // 构建画笔
        initPaint();
        // 由于view没有默认的padding需要设置预留显示图标
        setPadding();
    }

    /**
     * 定位文本的位置，让其居中
     */
    private void setTextLocation() {
        // fm=bmPaint.getFontMetrics();
        // 居中
        // float center_text=bmp_height/2-fm

    }

    private void setPadding() {
        switch (type) {
            case Gravity.TOP:
			/*
			 * setPadding((int) Math.ceil(bmp_width) / 2, (int)
			 * Math.ceil(bmp_height), (int) Math.ceil(bmp_width) / 2, 0);
			 */
                setPadding((int) Math.ceil(bmp_width) / 2,
                        (int) Math.ceil(bmp_height),
                        (int) Math.ceil(bmp_width) / 2, numTextSize + 5);
                break;

            case Gravity.BOTTOM:
                setPadding((int) Math.ceil(bmp_width) / 2, 0,
                        (int) Math.ceil(bmp_width) / 2, (int) Math.ceil(bmp_height));
                break;
        }

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {

            fm = bmPaint.getFontMetrics();
            double num = (getProgress() * 100 * getUnit() / getMax() + start);
            if (isRound){
                num = Math.round(num);
            }
            if (num >= end) {
                num = end;
                setProgress(100);
            }

            if (digit==0){
                numText = prefix_text + (int)num + numTextFormat;
            }else{
                BigDecimal bd_num = new BigDecimal(num);
                bd_num = bd_num.setScale(2,BigDecimal.ROUND_HALF_UP);
                numText = prefix_text + bd_num + numTextFormat;
            }
            numTextWidth = bmPaint.measureText(numText);
            this.num = num;
            rect_seek = this.getProgressDrawable().getBounds();
            float thum_height = 0;
            // api必须大于16
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rect_thum = this.getThumb().getBounds();
                thum_height = rect_thum.height();
            }

            // 计算bitmap左上的位置坐标
            float bm_x = rect_seek.width() * getProgress() / getMax();
            // 计算文字的中心位置在bitmap
            float text_x = rect_seek.width() * getProgress() / getMax()
                    + (bmp_width - numTextWidth) / 2;

            int margin = (getWindowWith()-getMeasuredWidth())/2;
            if (text_x <= 0) {
                text_x =  0;
            } else if (text_x + numTextWidth+margin+getPaddingRight()>= getWindowWith()) {
                text_x = getWindowWith() -numTextWidth-margin-getPaddingRight();
            }
            // 还应该减去文字的高度

            float xLeft = getPaddingLeft();
            float xRight = getMeasuredWidth() - getPaddingRight();
            if (isShowStartEndMarkLine) {
                Paint mPaintLeft = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaintLeft.setStrokeWidth(markLineHight);
                if (getProgress()<=3){
                    mPaintLeft.setColor(Color.TRANSPARENT);
                }else {
                    mPaintLeft.setColor(markLineColor);
                }
                canvas.drawLine(xLeft, getMeasuredHeight() - (numTextSize + 5)
                                - rect_thum.height() / 2+1, xLeft, getMeasuredHeight()
                                - rect_thum.height() / 2 - 20 - (numTextSize + 5),
                        mPaintLeft);

                Paint mPaintRight = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaintRight.setColor(markLineColor);
                mPaintRight.setStrokeWidth(markLineHight);
                if (getProgress()<=97){
                    mPaintRight.setColor(Color.parseColor("#E2E2E2"));
                }else {
                    mPaintRight.setColor(Color.TRANSPARENT);
                }
                canvas.drawLine(xRight,
                        getMeasuredHeight() - rect_thum.height() / 2
                                - (numTextSize + 5)+1, xRight,
                        getMeasuredHeight() - rect_thum.height() / 2 - 20
                                - (numTextSize + 5), mPaintRight);
            }

            float text_y = bmp_height / 2;
            float text_center = bmp_height / 2 - fm.descent
                    + (fm.descent - fm.ascent) / 2;
            switch (type) {
                case Gravity.TOP:
                    if (isShowTxtBg) {
                        canvas.drawBitmap(bm, bm_x, 0, bmPaint);
                        // img_height / 2 - fm.descent + (fm.descent - fm.ascent) /
                        // 2
                        canvas.drawText(
                                numText,
                                text_x,
                                (float) (bmp_height
                                        / 2
                                        - (fm.descent - (fm.descent - fm.ascent) / 2) - (bmp_height * numScale) / 2),
                                bmPaint);
                    } else {
                        canvas.drawText(
                                numText,
                                text_x,
                                (float) (bmp_height
                                        / 2
                                        - (fm.descent - (fm.descent - fm.ascent) / 2) - (bmp_height * numScale) / 2),
                                bmPaint);
                    }
                    break;
                case Gravity.BOTTOM:
                    // +rect_thum.height()/2-rect_seek.height()/2
                    if (isShowTxtBg) {
                        canvas.drawBitmap(bm, bm_x, rect_thum.height(), bmPaint);
                        // canvas.drawText(numText,text_x, (float) (bmp_height / 2
                        // -( fm.descent -(fm.descent - fm.ascent) /
                        // 2)+rect_seek.height()+20+(bmp_height*numScale)/2),bmPaint);
                        canvas.drawText(
                                numText,
                                text_x,
                                (float) (thum_height + (bmp_height
                                        / 2
                                        - (fm.descent - (fm.descent - fm.ascent) / 2) + bmp_height
                                        * numScale / 2)), bmPaint);
                    } else {
                        canvas.drawText(
                                numText,
                                text_x,
                                (float) (thum_height + (bmp_height
                                        / 2
                                        - (fm.descent - (fm.descent - fm.ascent) / 2) + bmp_height
                                        * numScale / 2)), bmPaint);
                    }
                    break;
                default:
                    break;

            }
            if (isShowStartAndEnd) {
                Paint mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint1.setColor(startAndEndColor);
                mPaint1.setTypeface(Typeface.DEFAULT);
                mPaint1.setTextSize(numTextSize);
                String startText;
                String endText;
                if (digit > 0){
                    BigDecimal bd_start = new BigDecimal(start);
                    bd_start = bd_start.setScale(2,BigDecimal.ROUND_HALF_UP);
                    startText = bd_start + numTextFormat;

                    BigDecimal bd_end = new BigDecimal(end);
                    bd_end = bd_end.setScale(2,BigDecimal.ROUND_HALF_UP);
                    endText = bd_end + numTextFormat;
                }else{
                    startText = (int)start + numTextFormat;
                    endText = (int)end + numTextFormat;
                }
                float endTextWidth = mPaint1.measureText(endText);
                Paint.FontMetrics fm1 = mPaint1.getFontMetrics();
                canvas.drawText(startText, xLeft, getMeasuredHeight()
                        - (numTextSize + 5) + (rect_thum.height() / 2)
                        + fm1.top - fm1.ascent, mPaint1);
                Paint.FontMetrics fm2 = mPaint1.getFontMetrics();
                canvas.drawText(
                        endText,
                        xRight - endTextWidth,
                        getMeasuredHeight() - (numTextSize + 5)
                                + (rect_thum.height() / 2) + fm2.top
                                - fm2.ascent, mPaint1);
            }
            // 设置文本的位置
        } catch (Exception e) {
            // 为什么要try因为你的参数可能没有填
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }

    private void initPaint() {
        // 抗锯齿
        bmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmPaint.setTypeface(Typeface.DEFAULT);
        bmPaint.setTextSize(numTextSize);
        bmPaint.setColor(numTextColor);
    }

    private void initBm() {
        if (isShowTxtBg) {
            bm = BitmapFactory.decodeResource(getResources(), numbackground);
            // 注意判断是否是null
            if (bm != null) {
                bmp_width = bm.getWidth();
                bmp_height = bm.getHeight();
            }
        } else {
            bmp_width = 100;
            bmp_height = 100;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.CustomSeekBar);
        numTextFormat = array
                .getString(R.styleable.CustomSeekBar_numTextFormat);
        numbackground = array.getResourceId(
                R.styleable.CustomSeekBar_numbackground, R.drawable.shows);
        numTextSize = array.getDimensionPixelSize(
                R.styleable.CustomSeekBar_numTextSize, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                                getResources().getDisplayMetrics()));
        numTextColor = array.getColor(R.styleable.CustomSeekBar_numTextColor,
                Color.WHITE);
        type = array.getInt(R.styleable.CustomSeekBar_numType, Gravity.TOP);

        numScale = Double
                .parseDouble(array
                        .getString(R.styleable.CustomSeekBar_numScale) == null ? numScale
                        + ""
                        : array.getString(R.styleable.CustomSeekBar_numScale));
        numTextFormat = numTextFormat == null ? "%" : numTextFormat;

        isShowStartEndMarkLine = array.getBoolean(
                R.styleable.CustomSeekBar_isShow_Start_End_mark_line, false);
        isShowTxtBg = array.getBoolean(R.styleable.CustomSeekBar_isShowTxtBg,
                true);
        isRound = array.getBoolean(R.styleable.CustomSeekBar_isRound,
                false);
        markLineColor = array.getColor(R.styleable.CustomSeekBar_markLineColor,
                Color.BLACK);
        startAndEndColor = array.getColor(R.styleable.CustomSeekBar_startAndEndColor,
                Color.BLACK);
        prefix_text = array.getString(R.styleable.CustomSeekBar_prefix_text);
        digit = array.getInt(R.styleable.CustomSeekBar_digit,0);
        markLineHight = array.getDimensionPixelSize(
                R.styleable.CustomSeekBar_markLineHight, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_PX, 3,
                                getResources().getDisplayMetrics()));

        array.recycle();
    }

    public void setStartAndEnd(double start, double end) {
        isShowStartAndEnd = true;
        this.start = start;
        this.end = end;
        unit = (end - start) / 100;
        postInvalidate();
    }

    public String getNumText() {
        return numText;
    }

    public void setNumText(String numText) {
        this.numText = numText;
        invalidate();
    }

    public int getNumTextSize() {
        return numTextSize;
    }

    public void setNumTextSize(int numTextSize) {
        this.numTextSize = numTextSize;

    }

    public int getNumbackground() {
        return numbackground;
    }

    public void setNumbackground(int numbackground) {
        this.numbackground = numbackground;
    }

    public int getNumTextColor() {
        return numTextColor;
    }

    public void setNumTextColor(int numTextColor) {
        this.numTextColor = numTextColor;
    }

    public int getWindowWith() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public int getWindowHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
}