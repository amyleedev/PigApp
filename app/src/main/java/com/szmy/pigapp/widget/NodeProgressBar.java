package com.szmy.pigapp.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.szmy.pigapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 2015-1-24
 * 小林爱
 */
public class NodeProgressBar extends View implements Runnable {
    /**
     * 读取空进度条图片
     */
    private BitmapDrawable db_empty;
    /**
     * View宽度
     */
    private int viewWidth;
    /**
     * View高度
     */
    private int viewHeight;
    /**
     * 白色空心圆图片集合
     */
    private List<BitmapDrawable> list_whitecircle = new ArrayList<BitmapDrawable>();
    /**
     * 蓝色实心圆图片集合
     */
    private List<BitmapDrawable> list_bluecircle = new ArrayList<BitmapDrawable>();
    /**
     * 蓝色进度条
     */
    private BitmapDrawable db_blue;

    /**
     * 进度比值       控制蓝色进度条
     */
    private double ratio = 0;
    /**
     * 节点名称
     */
    private String[] textArr = new String[]{"发布信息", "下单", "买家付款", "后台确认", "后台付款", "交易完成"};
    /**
     * 节点数
     */
    private int count = textArr.length;
    ;
    /**
     * 当前节点进度
     */
    private int index = 0;
    /**
     * X轴对称 偏移值
     */
    private int offX = 50;
    /**
     * Y轴 偏移值
     */
    private int offY = 100;
    /**
     * 文字与节点 偏移值
     */
    private int offTextY = 85;
    /**
     * 白色空心圆偏移值
     */
    private int offWhiteCirle_y = -3;
    /**
     * 白色空心进度条偏移值
     */
    private int offWhiteRect_y = -2;
    /**
     * 白色空心进度条偏移值
     */
    private BitmapDrawable db_blue_half_circle;
    /**
     * 创建一只新画笔
     */
    private Paint paint = new Paint();
    /**
     * 白色空心圆半径
     */
    private int r_white = 46;
    /**
     * 蓝色空心圆半径
     */
    private int r_blue = 26;
    /**
     * 白色进度条高度
     */
    private int whiteProgressHeight = 15;
    /**
     * 蓝色进度条高度
     */
    private int blueProgressHeight = 16;

    /**
     * 文本颜色j
     */
    private String textColor = "#46A3FF";
    /**
     * 文本未激活颜色
     */
    private String textColorNotEnabled = "#7E7E81";
    /**
     * View 背景颜色
     */
    private String bgColor = "#FAFAFB";
    /**
     * 文本框大小
     */
    private int textSize = 23;

    /**
     * 白色空心进度条宽度
     */
    private int maxProgressWidth;
    /**
     * 半圆蓝色进度条宽度
     */
    private int half_blueWidth = 12;

    @SuppressWarnings("deprecation")
    public NodeProgressBar(Context context) {
        super(context);
        init();
    }

    public NodeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 控制蓝色进度条
     */
    public void setProgressOnly(int i) {
        ratio = i / 100d;
        invalidate();
    }

    /**
     * 以节点数来空值进度条 至少大于1
     */
    public void setProgressByNode(final double d) {
        int progress;
        if (d == 1) {
            progress = 1;
        } else {
            progress = (int) ((100d / ((count - 1) * 1.0d)) * (d - 1));
        }
        setProgressAndIndex(progress);

    }

    /**
     * 控制蓝色进度条并且对节点染色
     */
    public void setProgressAndIndex(int i) {
        if (i == 0) {
            index = 0;
            ratio = 0;
            invalidate();
            return;
        }
        //获得相对进度条长度
        int adbProgress = maxProgressWidth - (count - 1) * r_white;
        //得到每一个节点所需进度值
        int k = 100 / (count - 1);
        //计算当前进度需要染色的节点个数
        index = 1 + i / k;
        if (index != count) {
            //获得节点磁力比率
            double wh = 1.0d * (r_white / 2) / (double) maxProgressWidth;
            //计算蓝色进度条和染色节点宽度
            ratio = i % 100 == 0 ? ratio = 1 : wh + wh * 2 * (index - 1) + 1.0d * ((double) adbProgress / (double) maxProgressWidth) * (i / 100d);
        } else {
            //设置进度条为满
            ratio = 1;
        }
        invalidate();
    }

    /**
     * 初始化图片资源，和基础数值
     */
    @SuppressWarnings("deprecation")
    private void init() {
        //根据节点个数 初始化空心圆和实心圆
        for (int i = 0; i < count; i++) {
            BitmapDrawable drawable1 = new BitmapDrawable(
                    BitmapFactory.decodeResource(getResources(),
                            R.drawable.progress_white_circle));
            list_whitecircle.add(drawable1);
            BitmapDrawable drawable2 = new BitmapDrawable(
                    BitmapFactory.decodeResource(getResources(),
                            R.drawable.progress_blue_circle));
            list_bluecircle.add(drawable2);
        }
        //初始化蓝色小半圆
        db_blue_half_circle = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_blue_half_circle));
        //初始化空心进度条
        db_empty = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_whtie_groove));
        //初始化
        db_blue = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), R.drawable.progress_blue_groove));
        //UI线程初始化数值
        this.post(this);
    }

    @Override
    public void run() {
        //读取View宽度
        viewWidth = NodeProgressBar.this.getWidth();
        //读取View高度
        viewHeight = NodeProgressBar.this.getHeight();
        // 进度条宽度计算
        maxProgressWidth = viewWidth - r_white - offX * 2;
        //绘制
        invalidate();
    }


    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        //获得X轴偏转值
        int offAbs_x = (int) ((viewWidth - maxProgressWidth) / 2.0d);
        //获得X轴偏转值
        canvas.drawColor(Color.parseColor(bgColor));
        //绘制空心进度条
        drawRect(canvas, db_empty, viewWidth / 2, r_white / 2 + offY + offWhiteRect_y, maxProgressWidth, whiteProgressHeight);

        paint.setTextSize(textSize);
        paint.setFakeBoldText(true);

        //绘制白色空心园
        for (int i = 0, j = list_whitecircle.size(); i < j; i++) {
            BitmapDrawable db_whitecircle = list_whitecircle.get(i);
            int x = maxProgressWidth / (count - 1) * i + offAbs_x;
            int y = r_white / 2 + offWhiteCirle_y + offY;
            drawCircle(canvas, db_whitecircle, x, y, r_white);

            String str = textArr[i];
            if (i < index) {
                paint.setColor(Color.parseColor(textColor));
            } else {
                paint.setColor(Color.parseColor(textColorNotEnabled));
            }
            float textWidht = paint.measureText(str);
            canvas.drawText(str, x - textWidht / 2, y + offTextY, paint);
        }

        //绘制蓝色进度条
        drawRect(canvas, db_blue, (int)
                        ((maxProgressWidth * ratio) / 2) + offAbs_x,
                r_white / 2 + offY,
                (int) (maxProgressWidth * ratio), blueProgressHeight);

        //绘制蓝色小半圆
        if (ratio > 0) {

            drawRect(canvas, db_blue_half_circle, (int) ((maxProgressWidth * ratio) / 2)
                            + (int) (maxProgressWidth * ratio) / 2 + half_blueWidth
                            / 2 + offAbs_x, r_white / 2 + offY, half_blueWidth,
                    blueProgressHeight);
        }
        //绘制蓝色圆
        for (int i = 0, j = index; i < j; i++) {
            BitmapDrawable db_bluecircle = list_bluecircle.get(i);
            drawCircle(canvas, db_bluecircle,
                    maxProgressWidth / (count - 1) * i + offAbs_x, r_white / 2 + offY, r_blue);
        }

    }

    /**
     * 传统矩形坐标方法
     */
    public void drawRect(Canvas canvas, Drawable d, int x, int y, int width,
                         int height) {
        d.setBounds(x - width / 2, y - height / 2, x + width / 2, y + height
                / 2);
        d.draw(canvas);
    }

    /**
     * 传统圆点坐标计算方法
     */
    public void drawCircle(Canvas canvas, Drawable d, int x, int y, int r) {
        d.setBounds(x - r / 2, y - r / 2, x + r / 2, y + r / 2);
        d.draw(canvas);
    }

    /**
     * 绘制文字 传统坐标计算方法
     */
    public void drawText(Canvas canvas, Paint paint, String str, int x, int y, int w, int h) {
//        paint.setTextSize(textSize);//设置字体大小
        canvas.drawText(str, x - w / 2, y - h / 2, x + w, y + h, paint);
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


}
