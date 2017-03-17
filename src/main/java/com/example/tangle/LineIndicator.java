package com.example.tangle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * create by  hqc  on 2015-5-18
 * 仿微信的下的图标 变色 view
 */
public class LineIndicator extends View  implements  ViewPager.OnPageChangeListener{

    private  static final  String TAG="LineIndicator";

    private static int DEFAULT_HEIGHT=6;// dp
    private static int DEFAULT_WIDTH=48;// dp

    private ViewPager mViewpager;

    /*** 当前 下标*/
    private int mCurrentPosition=0;


    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;

    private int selectedColor=Color.RED;
    private int unSelectedColor=Color.WHITE;

    private int mNextPosition;
    private float mSelectionOffset;
    private boolean  isMoveRight=true;

    private  Paint paint;


    public LineIndicator(Context context) {
        super(context);
        init();
    }

    public LineIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LineIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void  init(){
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);//充满
        paint.setAntiAlias(true);// 设置画笔的锯齿效果

        mIndicatorWidth=dip2px(DEFAULT_WIDTH);
        mIndicatorHeight=dip2px(DEFAULT_HEIGHT);
        mIndicatorMargin=dip2px(DEFAULT_WIDTH);
    }

    public int getmIndicatorMargin() {
        return mIndicatorMargin;
    }

    public void setmIndicatorMargin(int mIndicatorMargin) {
        this.mIndicatorMargin = mIndicatorMargin;
    }

    public int getmIndicatorHeight() {
        return mIndicatorHeight;
    }

    public void setmIndicatorHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }

    public int getmIndicatorWidth() {
        return mIndicatorWidth;
    }

    public void setmIndicatorWidth(int mIndicatorWidth) {
        this.mIndicatorWidth = mIndicatorWidth;
    }

    public int getUnSelectedColor() {
        return unSelectedColor;
    }

    public void setUnSelectedColor(int unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * 设置 viewpager
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager) {
        mViewpager = viewPager;
        mCurrentPosition = mViewpager.getCurrentItem();
        mNextPosition =-1;
        mViewpager.addOnPageChangeListener(this);

        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),dip2px(mIndicatorHeight*2));
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(positionOffset>0f){
            Log.d(TAG, mCurrentPosition +"  onPageScrolled  "+position +" "+positionOffset +" "+positionOffsetPixels);
            if(mCurrentPosition==position){//右移动
                isMoveRight=true;
                mNextPosition =position+1;
            }else { // 左移动
                isMoveRight=false;
                mNextPosition =position;
            }

            mSelectionOffset=positionOffset;
            invalidate();
        }else {
            mCurrentPosition=position;
            mSelectionOffset=0;
            mNextPosition =-1;
        }


    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mViewpager==null||mViewpager.getAdapter()==null){
            return;
        }
        int itemLen=mViewpager.getAdapter().getCount();
        // 计算宽度，，
        int width=(itemLen+1)*mIndicatorWidth + (itemLen-1)*mIndicatorMargin;
        float startX= (float) ((getMeasuredWidth()-width)*0.5);
        float startY= (float) ((getMeasuredHeight()-mIndicatorHeight)*0.5);

         float sx=startX;
        for(int i=0;i<itemLen;i++){
            int itemWidth=mIndicatorWidth;

            if(mCurrentPosition==i|| mNextPosition ==i){
                if(mCurrentPosition==i){
                    if(isMoveRight){
                        int  color = blendColors(selectedColor, unSelectedColor, 1-mSelectionOffset);
                        paint.setColor(color);
                        itemWidth= (int) (mIndicatorWidth*(2-mSelectionOffset));
                    }else {
                        int  color = blendColors(selectedColor, unSelectedColor, mSelectionOffset);
                        paint.setColor(color);
                        itemWidth= (int) (mIndicatorWidth*(2-(1-mSelectionOffset)));
                    }
                }
                if(mNextPosition ==i){//选中

                    if(isMoveRight){
                        int  color = blendColors(selectedColor, unSelectedColor, mSelectionOffset);
                        paint.setColor(color);
                        itemWidth= (int) (mIndicatorWidth*(1+mSelectionOffset));
                    }else {
                        int  color = blendColors(selectedColor, unSelectedColor,1-mSelectionOffset);
                        paint.setColor(color);
                        itemWidth= (int) (mIndicatorWidth*(1+(1-mSelectionOffset)));
                    }
                }
            }else {
                paint.setColor(unSelectedColor);
            }


            RectF oval3 = new RectF(sx, startY, sx+itemWidth, startY+mIndicatorHeight);// 设置个新的长方形
            canvas.drawRoundRect(oval3, (float) (mIndicatorHeight*0.5), (float) (mIndicatorHeight*0.5), paint);//第
            sx=sx+itemWidth+mIndicatorMargin;
        }
    }


    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * 画圆角矩形
     * @param canvas
     */
    private void  drawRoundRect(Canvas canvas,int position){
        if(mCurrentPosition==position){//选中
             paint.setColor(Color.RED);
        }else {
             paint.setColor(Color.WHITE);
        }

        RectF oval3 = new RectF((mIndicatorWidth+mIndicatorMargin)*position, 0, (mIndicatorWidth+80)*position+mIndicatorWidth, mIndicatorHeight);// 设置个新的长方形
        canvas.drawRoundRect(oval3, (float) (mIndicatorHeight*0.5), (float) (mIndicatorHeight*0.5), paint);//第二个参数是x半径，第三个参数是y半径
    }
}
