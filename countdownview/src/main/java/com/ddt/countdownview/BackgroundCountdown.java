package com.ddt.countdownview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 * Background Countdown
 * Created by iWgang on 16/6/19.
 * https://github.com/iwgang/CountdownView
 */
class BackgroundCountdown extends BaseCountdown {
    private static final float DEFAULT_TIME_BG_DIVISION_LINE_SIZE = 0.5f; // dp

    private boolean isShowTimeBgDivisionLine;
    private int mTimeBgDivisionLineColor;
    private float mTimeBgDivisionLineSize;
    private float mTimeBgRadius;
    private float mTimeBgSize;
    private int mTimeBgColor;
    private Paint mTimeTextBgPaint;
    private Paint mTimeTextBgDivisionLinePaint;
    private float mDefSetTimeBgSize;
    private float mDayTimeBgWidth;
    private RectF mDayBgRectF, mHourBgRectF, mMinuteBgRectF, mSecondBgRectF, mMillisecondBgRectF;
    private RectF mDayBgRectF1, mHourBgRectF1, mMinuteBgRectF1, mSecondBgRectF1, mMillisecondBgRectF1;
    private float mTimeBgDivisionLineYPos;
    private float mTimeTextBaseY;

    @Override
    public void initStyleAttr(Context context, TypedArray ta) {
        super.initStyleAttr(context, ta);

        mTimeBgColor = ta.getColor(R.styleable.CountdownView_timeBgColor, 0xFF444444);
        mTimeBgRadius = ta.getDimension(R.styleable.CountdownView_timeBgRadius, 0);
        isShowTimeBgDivisionLine = ta.getBoolean(R.styleable.CountdownView_isShowTimeBgDivisionLine, true);
        mTimeBgDivisionLineColor = ta.getColor(R.styleable.CountdownView_timeBgDivisionLineColor, Color.parseColor("#30FFFFFF"));
        mTimeBgDivisionLineSize = ta.getDimension(R.styleable.CountdownView_timeBgDivisionLineSize, Utils.dp2px(context, DEFAULT_TIME_BG_DIVISION_LINE_SIZE));
        mTimeBgSize = ta.getDimension(R.styleable.CountdownView_timeBgSize, 0);
        mDefSetTimeBgSize = mTimeBgSize;
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        // time background
        mTimeTextBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextBgPaint.setStyle(Paint.Style.FILL);
        mTimeTextBgPaint.setColor(mTimeBgColor);

        // time background division line
        if (isShowTimeBgDivisionLine) {
            initTimeTextBgDivisionLinePaint();
        }
    }

    private void initTimeTextBgDivisionLinePaint() {
        mTimeTextBgDivisionLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextBgDivisionLinePaint.setColor(mTimeBgDivisionLineColor);
        mTimeTextBgDivisionLinePaint.setStrokeWidth(mTimeBgDivisionLineSize);
    }

    @Override
    protected void initTimeTextBaseInfo() {
        Rect rect = new Rect();
        mTimeTextPaint.getTextBounds("0", 0, 1, rect);
        mTimeTextWidth = rect.width();
        mTimeTextHeight = rect.height();
        mTimeTextBottom = rect.bottom;
        if (mDefSetTimeBgSize == 0 || mTimeBgSize < mTimeTextWidth) {
            mTimeBgSize = mTimeTextWidth + (Utils.dp2px(mContext, 2) * 2);
        }
    }

    private static int timeSpacing = 2;
    private static float topAndBottomPadding = 4;
    private static float textTopPadding = 4;
    /**
     * initialize time initialize rectF
     */
    private void initTimeBgRect(float topPaddingSize) {
    	topPaddingSize = topPaddingSize+topAndBottomPadding;
        float mHourLeft;
        float mMinuteLeft;
        float mSecondLeft;
        boolean isInitHasBackgroundTextBaseY = false;
        float mDayTimeBgWidth = 2*this.mDayTimeBgWidth/3;
        float mTimeBgSize = 2*this.mTimeBgSize/3;
        if (isShowDay) {
            // initialize day background rectF

            mDayBgRectF = new RectF(mLeftPaddingSize, topPaddingSize-textTopPadding, mLeftPaddingSize + mDayTimeBgWidth, topPaddingSize + mDayTimeBgWidth+textTopPadding);
            mDayBgRectF1 = new RectF(mLeftPaddingSize + mDayTimeBgWidth + timeSpacing, topPaddingSize-textTopPadding, mLeftPaddingSize + 2*mDayTimeBgWidth + timeSpacing, topPaddingSize + mDayTimeBgWidth+textTopPadding);
            // hour left point
            mHourLeft = mLeftPaddingSize + 2*mDayTimeBgWidth+timeSpacing + mSuffixDayTextWidth + mSuffixDayLeftMargin + mSuffixDayRightMargin;

            if (!isShowHour && !isShowMinute && !isShowSecond) {
                isInitHasBackgroundTextBaseY = true;
                initHasBackgroundTextBaseY(mDayBgRectF);
            }
        } else {
            // hour left point
            mHourLeft = mLeftPaddingSize;
        }

        if (isShowHour) {
            // initialize hour background rectF
            mHourBgRectF = new RectF(mHourLeft, topPaddingSize-textTopPadding, mHourLeft + mTimeBgSize, topPaddingSize + mTimeBgSize+textTopPadding);
            mHourBgRectF1 = new RectF(mHourLeft + mTimeBgSize + timeSpacing, topPaddingSize-textTopPadding, mHourLeft + 2*mTimeBgSize+timeSpacing, topPaddingSize + mTimeBgSize+textTopPadding);
            // minute left point
            mMinuteLeft = mHourLeft + 2*mTimeBgSize+2 + mSuffixHourTextWidth + mSuffixHourLeftMargin + mSuffixHourRightMargin;

            if (!isInitHasBackgroundTextBaseY) {
                isInitHasBackgroundTextBaseY = true;
                initHasBackgroundTextBaseY(mHourBgRectF);
            }
        } else {
            // minute left point
            mMinuteLeft = mHourLeft;
        }

        if (isShowMinute) {
            // initialize minute background rectF
            mMinuteBgRectF = new RectF(mMinuteLeft, topPaddingSize-textTopPadding, mMinuteLeft + mTimeBgSize, topPaddingSize + mTimeBgSize+textTopPadding);
            mMinuteBgRectF1 = new RectF(mMinuteLeft + mTimeBgSize + timeSpacing, topPaddingSize-textTopPadding, mMinuteLeft + 2*mTimeBgSize+timeSpacing, topPaddingSize + mTimeBgSize+textTopPadding);
            // second left point
            mSecondLeft = mMinuteLeft + 2*mTimeBgSize+2 + mSuffixMinuteTextWidth + mSuffixMinuteLeftMargin + mSuffixMinuteRightMargin;

            if (!isInitHasBackgroundTextBaseY) {
                isInitHasBackgroundTextBaseY = true;
                initHasBackgroundTextBaseY(mMinuteBgRectF);
            }
        } else {
            // second left point
            mSecondLeft = mMinuteLeft;
        }

        if (isShowSecond) {
            // initialize second background rectF
            mSecondBgRectF = new RectF(mSecondLeft, topPaddingSize-textTopPadding, mSecondLeft + mTimeBgSize, topPaddingSize + mTimeBgSize+textTopPadding);
            mSecondBgRectF1 = new RectF(mSecondLeft + mTimeBgSize + timeSpacing, topPaddingSize-textTopPadding, mSecondLeft + 2*mTimeBgSize+timeSpacing, topPaddingSize + mTimeBgSize+textTopPadding);

            if (isShowMillisecond) {
                // millisecond left point
                float mMillisecondLeft = mSecondLeft + 2*mTimeBgSize+2 + mSuffixSecondTextWidth + mSuffixSecondLeftMargin + mSuffixSecondRightMargin;

                // initialize millisecond background rectF
                mMillisecondBgRectF = new RectF(mMillisecondLeft, topPaddingSize-textTopPadding, mMillisecondLeft + mTimeBgSize, topPaddingSize + mTimeBgSize+textTopPadding);
                mMillisecondBgRectF1 = new RectF(mMillisecondLeft + mTimeBgSize + timeSpacing, topPaddingSize-textTopPadding, mMillisecondLeft + 2*mTimeBgSize+timeSpacing, topPaddingSize + mTimeBgSize+textTopPadding);
            }

            if (!isInitHasBackgroundTextBaseY) {
                initHasBackgroundTextBaseY(mSecondBgRectF);
            }
        }
    }

    private float getSuffixTextBaseLine(String suffixText, float topPaddingSize) {
        Rect tempRect = new Rect();
        mSuffixTextPaint.getTextBounds(suffixText, 0, suffixText.length(), tempRect);

        float ret;
        switch (mSuffixGravity) {
            case 0:
                // top
                ret = topPaddingSize - tempRect.top;
                break;
            default:
            case 1:
                // center
                ret = topPaddingSize + mTimeBgSize - mTimeBgSize / 2  /*+ tempRect.height() / 2*/;
                break;
            case 2:
                // bottom
                ret = topPaddingSize + mTimeBgSize - tempRect.bottom;
                break;
        }

        return ret;
    }

    private void initHasBackgroundTextBaseY(RectF rectF) {
        // time text baseline
        Paint.FontMetrics timeFontMetrics = mTimeTextPaint.getFontMetrics();
        mTimeTextBaseY = rectF.top + (rectF.bottom - rectF.top - timeFontMetrics.bottom + timeFontMetrics.top) / 2 - timeFontMetrics.top - mTimeTextBottom;
        // initialize background division line y point
        mTimeBgDivisionLineYPos = rectF.centerY() + (mTimeBgDivisionLineSize == Utils.dp2px(mContext, DEFAULT_TIME_BG_DIVISION_LINE_SIZE) ? mTimeBgDivisionLineSize : mTimeBgDivisionLineSize / 2);
    }

    /**
     * initialize time text baseline
     * and
     * time background top padding
     */
    private float initTimeTextBaselineAndTimeBgTopPadding(int viewHeight, int viewPaddingTop, int viewPaddingBottom, int contentAllHeight) {
        float topPaddingSize;
        if (viewPaddingTop == viewPaddingBottom) {
            // center
            topPaddingSize = (viewHeight - contentAllHeight) / 2;
        } else {
            // padding top
            topPaddingSize = viewPaddingTop;
        }

        if (isShowDay && mSuffixDayTextWidth > 0) {
            mSuffixDayTextBaseline = getSuffixTextBaseLine(mSuffixDay, topPaddingSize);
        }

        if (isShowHour && mSuffixHourTextWidth > 0) {
            mSuffixHourTextBaseline = getSuffixTextBaseLine(mSuffixHour, topPaddingSize);
        }

        if (isShowMinute && mSuffixMinuteTextWidth > 0) {
            mSuffixMinuteTextBaseline = getSuffixTextBaseLine(mSuffixMinute, topPaddingSize);
        }

        if (mSuffixSecondTextWidth > 0) {
            mSuffixSecondTextBaseline = getSuffixTextBaseLine(mSuffixSecond, topPaddingSize);
        }

        if (isShowMillisecond && mSuffixMillisecondTextWidth > 0) {
            mSuffixMillisecondTextBaseline = getSuffixTextBaseLine(mSuffixMillisecond, topPaddingSize);
        }

        return topPaddingSize;
    }

    @Override
    public int getAllContentWidth() {
//    	float width = getAllContentWidthBase(mTimeBgSize);
    	float width = getAllContentWidthBase(2*2*mTimeBgSize/3+2);

        if (isShowDay) {
            if (isDayLargeNinetyNine) {
                Rect rect = new Rect();
                String tempDay = String.valueOf(mDay);
                mTimeTextPaint.getTextBounds(tempDay, 0, tempDay.length(), rect);
                mDayTimeBgWidth = rect.width() + (Utils.dp2px(mContext, 2) * 4);
                width += mDayTimeBgWidth;
            } else {
                mDayTimeBgWidth = 2*2*mTimeBgSize/3+2;
                width += 2*2*mTimeBgSize/3+2;
            }
        }

        return (int)Math.ceil(width);
    }

    @Override
    public int getAllContentHeight() {
        return (int) mTimeBgSize;
    }

    @Override
    public void onMeasure(View v, int viewWidth, int viewHeight, int allContentWidth, int allContentHeight) {
        float retTopPaddingSize = initTimeTextBaselineAndTimeBgTopPadding(viewHeight, v.getPaddingTop(), v.getPaddingBottom(), allContentHeight);
//        mLeftPaddingSize = v.getPaddingLeft() == v.getPaddingRight() ?  (viewWidth - allContentWidth) / 2 : v.getPaddingLeft();
        mLeftPaddingSize = v.getPaddingLeft() == v.getPaddingRight() ?  (viewWidth - allContentWidth) / 2 : v.getPaddingLeft();
//        Log.e("v.getPaddingLeft() == v.getPaddingRight()", (v.getPaddingLeft() == v.getPaddingRight())+"");
//        Log.e("v.getPaddingLeft()", v.getPaddingLeft()+"");
        initTimeBgRect(retTopPaddingSize);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // show background
        float mHourLeft;
        float mMinuteLeft;
        float mSecondLeft;
        float mDayTimeBgWidth = 2*this.mDayTimeBgWidth/3;
        float mTimeBgSize = 2*this.mTimeBgSize/3;
//        mTimeTextBaseY = mTimeTextBaseY+2;
//        Log.e("显示的时间控件", ""+isShowDay+isShowHour+isShowMinute+isShowSecond+isShowMillisecond);
        if (isShowDay) {
            // onDraw day background
            canvas.drawRoundRect(mDayBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            canvas.drawRoundRect(mDayBgRectF1, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            if (isShowTimeBgDivisionLine) {
                // onDraw day background division line
                canvas.drawLine(mLeftPaddingSize, mTimeBgDivisionLineYPos, mLeftPaddingSize + 2*mDayTimeBgWidth+timeSpacing, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
            }
            // onDraw day text
            String[] dayData = Utils.formatNumBackground(mDay);
            canvas.drawText(dayData[0], mDayBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
            canvas.drawText(dayData[1], mDayBgRectF1.centerX(), mTimeTextBaseY, mTimeTextPaint);
            if (mSuffixDayTextWidth > 0) {
                // onDraw day suffix
                canvas.drawText(mSuffixDay, mLeftPaddingSize + 2*mDayTimeBgWidth+timeSpacing + mSuffixDayLeftMargin, mSuffixDayTextBaseline, mSuffixTextPaint);
            }

            // hour left point
            mHourLeft = mLeftPaddingSize + 2*mDayTimeBgWidth+timeSpacing + mSuffixDayTextWidth + mSuffixDayLeftMargin + mSuffixDayRightMargin;
        } else {
            // hour left point
            mHourLeft = mLeftPaddingSize;
        }

        if (isShowHour) {
            // onDraw hour background
            canvas.drawRoundRect(mHourBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            canvas.drawRoundRect(mHourBgRectF1, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            if (isShowTimeBgDivisionLine) {
                // onDraw hour background division line
                canvas.drawLine(mHourLeft, mTimeBgDivisionLineYPos, 2*mTimeBgSize+timeSpacing + mHourLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
            }
            // onDraw hour text
            String[] hourData = Utils.formatNumBackground(mHour);
            canvas.drawText(hourData[0], mHourBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
            canvas.drawText(hourData[1], mHourBgRectF1.centerX(), mTimeTextBaseY, mTimeTextPaint);
            if (mSuffixHourTextWidth > 0) {
                // onDraw hour suffix
                canvas.drawText(mSuffixHour, mHourLeft + 2*mTimeBgSize+timeSpacing + mSuffixHourLeftMargin, mSuffixHourTextBaseline, mSuffixTextPaint);
            }

            // minute left point
            mMinuteLeft = mHourLeft + 2*mTimeBgSize+timeSpacing + mSuffixHourTextWidth + mSuffixHourLeftMargin + mSuffixHourRightMargin;
        } else {
            // minute left point
            mMinuteLeft = mHourLeft;
        }

        if (isShowMinute) {
            // onDraw minute background
            canvas.drawRoundRect(mMinuteBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            canvas.drawRoundRect(mMinuteBgRectF1, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            if (isShowTimeBgDivisionLine) {
                // onDraw minute background division line
                canvas.drawLine(mTimeBgSize + mMinuteLeft, mTimeBgDivisionLineYPos, 2*mTimeBgSize+timeSpacing + mMinuteLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
            }
            // onDraw minute text
            String[] minuteData = Utils.formatNumBackground(mMinute);
            canvas.drawText(minuteData[0], mMinuteBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
            canvas.drawText(minuteData[1], mMinuteBgRectF1.centerX(), mTimeTextBaseY, mTimeTextPaint);
            if (mSuffixMinuteTextWidth > 0) {
                // onDraw minute suffix
                canvas.drawText(mSuffixMinute, mMinuteLeft + 2*mTimeBgSize+timeSpacing + mSuffixMinuteLeftMargin, mSuffixMinuteTextBaseline, mSuffixTextPaint);
            }

            // second left point
            mSecondLeft = mMinuteLeft + 2*mTimeBgSize+timeSpacing + mSuffixMinuteTextWidth + mSuffixMinuteLeftMargin + mSuffixMinuteRightMargin;
        } else {
            // second left point
            mSecondLeft = mMinuteLeft;
        }

        if (isShowSecond) {
            // onDraw second background
            canvas.drawRoundRect(mSecondBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            canvas.drawRoundRect(mSecondBgRectF1, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            if (isShowTimeBgDivisionLine) {
                // onDraw second background division line
//                canvas.drawLine(mSecondLeft, mTimeBgDivisionLineYPos, mTimeBgSize + mSecondLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
                canvas.drawLine(mSecondLeft, mTimeBgDivisionLineYPos, 2*mTimeBgSize+timeSpacing + mSecondLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
            }
            // onDraw second text
            String[] secondData = Utils.formatNumBackground(mSecond);
            canvas.drawText(secondData[0], mSecondBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
            canvas.drawText(secondData[1], mSecondBgRectF1.centerX(), mTimeTextBaseY, mTimeTextPaint);
            if (mSuffixSecondTextWidth > 0) {
                // onDraw second suffix
                canvas.drawText(mSuffixSecond, mSecondLeft + 2*mTimeBgSize+timeSpacing + mSuffixSecondLeftMargin, mSuffixSecondTextBaseline, mSuffixTextPaint);
            }

            if (isShowMillisecond) {
                // millisecond left point
                float mMillisecondLeft = mSecondLeft + 2*mTimeBgSize+timeSpacing+ mSuffixSecondTextWidth + mSuffixSecondLeftMargin + mSuffixSecondRightMargin;
                // onDraw millisecond background
                canvas.drawRoundRect(mMillisecondBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
                canvas.drawRoundRect(mMillisecondBgRectF1, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
                if (isShowTimeBgDivisionLine) {
                    // onDraw millisecond background division line
                    canvas.drawLine(mMillisecondLeft, mTimeBgDivisionLineYPos, 2*mTimeBgSize+timeSpacing + mMillisecondLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
                }
                // onDraw millisecond text
                String[] millisecondData = Utils.formatMillisecondBackground(mMillisecond);
                canvas.drawText(millisecondData[0], mMillisecondBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
                canvas.drawText(millisecondData[1], mMillisecondBgRectF1.centerX(), mTimeTextBaseY, mTimeTextPaint);
//                if (mSuffixMillisecondTextWidth > 0) {
//                    // onDraw millisecond suffix
//                    canvas.drawText(mSuffixMillisecond, mMillisecondLeft + 2*mTimeBgSize+timeSpacing + mSuffixMillisecondLeftMargin, mSuffixMillisecondTextBaseline, mSuffixTextPaint);
//                }
            }
        }
    }

    public void setTimeBgSize(float size) {
        mTimeBgSize = Utils.dp2px(mContext, size);
    }


    public void setTimeBgColor(int textColor) {
        mTimeBgColor = textColor;
        mTimeTextBgPaint.setColor(mTimeBgColor);
    }

    public void setTimeBgRadius(float radius) {
        mTimeBgRadius = Utils.dp2px(mContext, radius);
    }

    public void setIsShowTimeBgDivisionLine(boolean isShow) {
        isShowTimeBgDivisionLine = isShow;
        if (isShowTimeBgDivisionLine) {
            initTimeTextBgDivisionLinePaint();
        } else {
            mTimeTextBgDivisionLinePaint = null;
        }
    }

    public void setTimeBgDivisionLineColor(int textColor) {
        if (null != mTimeTextBgDivisionLinePaint) {
            mTimeBgDivisionLineColor = textColor;
            mTimeTextBgDivisionLinePaint.setColor(mTimeBgDivisionLineColor);
        }
    }

    public void setTimeBgDivisionLineSize(float size) {
        if (null != mTimeTextBgDivisionLinePaint) {
            mTimeBgDivisionLineSize = size;
            mTimeTextBgDivisionLinePaint.setStrokeWidth(mTimeBgDivisionLineSize);
        }
    }

}
