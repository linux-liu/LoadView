package com.liuxin.loadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
/**
 * Created by 刘信 on 2018/4/27.
 */

public class LoadView extends View {
    //已经load的颜色值
    private int mHasLoadColor;
    //没有load的颜色值
    private int mNormalLoadColor;
    //小长方形的宽度
    private int mRectangleWidth;
    //小长方形的高度
    private int mRectangleHeight;
    //小长方形的个数
    private int mRectangleNum;
    //小长方形圆角度
    private int mRectangleRadius;
    //内半径大小
    //内半径指不包括方块的大小
    private int mInnerRadius;
    //外半径
    private int mOuterRadius;
    //文字大小
    private int mTextSize;
    //画笔
    private Paint mPaint;
    private TextPaint mTextPaint;
   //小长方形
    private RectF mRectF;

    private Context mContext;
    //百分比字符串
    private String mPercentStr;
    //百分比
    private float mPercent;


    public LoadView(Context context) {
        this(context, null);
    }

    public LoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int deStyleAttr) {
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LoadView, deStyleAttr, 0);
        mHasLoadColor = mContext.getResources().getColor(R.color.color_3398ab);
        mNormalLoadColor = mContext.getResources().getColor(R.color.color_e0e0e0);
        mRectangleWidth = dipToPixels(mContext, 14);
        mRectangleHeight = dipToPixels(mContext, 28);
        mRectangleNum = 12;
        mInnerRadius = dipToPixels(mContext, 85);
        mTextSize = dipToPixels(mContext, 48);
        mRectangleRadius = dipToPixels(mContext, 3);
        mPercentStr = "0%";
        mPercent = 0f;
        if (typedArray != null) {
            //设置默认值
            mHasLoadColor = typedArray.getColor(R.styleable.LoadView_HasLoadColor, mHasLoadColor);
            mNormalLoadColor = typedArray.getColor(R.styleable.LoadView_NormalLoadColor, mNormalLoadColor);
            mRectangleWidth = typedArray.getDimensionPixelSize(R.styleable.LoadView_RectangleWidth, mRectangleWidth);
            mRectangleHeight = typedArray.getDimensionPixelSize(R.styleable.LoadView_RectangleHeight, mRectangleHeight);
            mRectangleNum = typedArray.getInteger(R.styleable.LoadView_RectangleNum, mRectangleNum);
            mInnerRadius = typedArray.getDimensionPixelSize(R.styleable.LoadView_InnerRadius, mInnerRadius);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.LoadView_TextSize, mTextSize);
            mRectangleRadius = typedArray.getDimensionPixelSize(R.styleable.LoadView_RectangleRadius, mRectangleRadius);
            typedArray.recycle();
        }
        //外半径
        mOuterRadius = mInnerRadius + mRectangleHeight;
        mRectF = new RectF(mOuterRadius - mRectangleWidth / 2f, 0, mOuterRadius + mRectangleWidth / 2f, mRectangleHeight);
        mPaint = new Paint();
        mPaint.setColor(mNormalLoadColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint = new TextPaint();
        /*try {
            Typeface typeface = Typeface.createFromFile("fonts/MyriadPro-Bold.otf");
            mTextPaint.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mHasLoadColor);

    }

    private int dipToPixels(Context context, float dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int)(valueDips * SCALE + 0.5f);
        return valuePixels;
    }

    /**
     * 请在主线程调用,设置进度 0到1
     * @param percent
     */
    public void setPercent(@FloatRange(from=0.0, to=1.0) float percent){
        this.mPercent=percent;
        this.mPercentStr=Math.round(percent*100)+"%";
        Log.i("lx","setPercent"+mPercentStr);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //强制宽高度，在xml文件中设置宽高无效
        //设置大小请根据半径、小长方形大小来设置
        int defaultSize = 2 * (mInnerRadius + mRectangleHeight);
        setMeasuredDimension(defaultSize, defaultSize);
        Log.i("lx","onMeasure"+getMeasuredWidth()+";"+getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("lx","onDraw");
        int hasDownCount = 0;//已经下载百分比换算成需要画多少个已经load的长方形
        hasDownCount = Math.round(mPercent * mRectangleNum);//才用四舍五入
        canvas.save();
        float degress = 360.0f / mRectangleNum;
        for (int i = 0; i < mRectangleNum; i++) {
            if (hasDownCount > i) {
                mPaint.setColor(mHasLoadColor);
            } else {
                mPaint.setColor(mNormalLoadColor);
            }
            canvas.drawRoundRect(mRectF, mRectangleRadius, mRectangleRadius, mPaint);
            canvas.rotate(degress, mOuterRadius, mOuterRadius);
        }
        canvas.restore();

        float textWidth = mTextPaint.measureText(mPercentStr);
        //计算高度要注意了
        //drawText是以文本的BaseLine为准的
        //所以计算高度步骤：文本高度(descent-ascent)的一半减去descent；得到的结果
        // 是Baseline离中心圆点的偏移量，再加上半径就是高度了
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float diffBaseLine = (-(fontMetrics.ascent + fontMetrics.descent)) / 2;
        canvas.drawText(mPercentStr, mOuterRadius - textWidth / 2, mOuterRadius + diffBaseLine, mTextPaint);

    }
}
