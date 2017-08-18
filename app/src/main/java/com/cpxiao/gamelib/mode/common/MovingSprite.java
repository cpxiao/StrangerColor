package com.cpxiao.gamelib.mode.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 走直线的Sprite类，可设置速度及方向
 */
public class MovingSprite extends Sprite {
    //每帧移动的像素数,始终为正数
    private float speed = 0;
    //偏移角度，以水平向右为0，顺时针方向为正。
    private float angle = 90;

    private RectF mMoveRectF = new RectF();//精灵可移动的范围矩形，根据绘制矩形判断边界

    public MovingSprite() {
        super();
    }

    public MovingSprite(Bitmap bitmap) {
        super(bitmap);
    }

    public void setSpeedAndAngle(float speed, float angle) {
        this.speed = speed;
        this.angle = angle;
    }


    public float getSpeedX() {
        return (float) (Math.cos(Math.PI * angle / 180) * speed);
    }

    public float getSpeedY() {
        return (float) (Math.sin(Math.PI * angle / 180) * speed);
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint) {
        if (!isDestroyed()) {
            //移动speed像素
            moveBy(getSpeedX(), getSpeedY());
        }
    }

}