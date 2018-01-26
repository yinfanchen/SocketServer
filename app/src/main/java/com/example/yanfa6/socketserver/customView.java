package com.example.yanfa6.socketserver;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yanfa6 on 2017/4/12.
 */
public class customView extends View {
    private int custom_size;
    private int custom_background;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private float scale = 1f;
    private final int SIZE = 15;
    private final int DEFAULT_COLOR = Color.BLUE;
    private Context context;
    public customView(Context context){
        super(context);
        this.context=context;
    }
    public customView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.customView,defStyleAttr,R.style.AppTheme);

        custom_size = a.getDimensionPixelSize(R.styleable.customView_size,SIZE);
        custom_background = a.getColor(R.styleable.customView_backgroud_color,DEFAULT_COLOR);
        a.recycle();
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(custom_background);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    //学习自定义view 首先必须知道 控件是怎么绘制出来的过程
    //onMeasure-->onLayout-->Draw


    //onMeasure 首先需要把控件的宽高多少  测量view的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heigtSize = MeasureSpec.getSize(heightMeasureSpec);
        //定义两个int 类型的数据，measure获取到的数据，传到layout使用
        int measureHeight , measureWidth;
        //获取的height width需要根据mode的类型去设置
        if (widthMode == MeasureSpec.EXACTLY){
            measureWidth = widthSize;
        }else {
            measureWidth = SIZE;
        }

        if (heightMode == MeasureSpec.EXACTLY){
            measureHeight = heigtSize;
        }else {
            measureHeight = SIZE;
        }
        setMeasuredDimension(measureWidth,measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mWidth = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth/2,mHeight/2,custom_size * scale,mPaint);
    }

    private ValueAnimator mAnimator;

    public void startAnimation() {
        mAnimator = ValueAnimator.ofFloat(1, 2);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale =animation.getAnimatedFraction();
                postInvalidate();
            }
        });

        // 重复次数 -1表示无限循环
        mAnimator.setRepeatCount(-1);

        // 重复模式, RESTART: 重新开始 REVERSE:恢复初始状态再开始
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);

        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 关闭动画
        mAnimator.end();
    }

}
