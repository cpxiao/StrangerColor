package com.cpxiao.strangercolor.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/08/18.
 */

public class GameOverMsg extends Sprite {
    private String mScoreMsg;
    private String mBestScoreMsg;
    private String mTitle;
    private String mSubTitle;

    private RectF mRectF = new RectF();

    public void setScoreMsg(String scoreMsg) {
        mScoreMsg = scoreMsg;
    }

    public void setBestScoreMsg(String bestScoreMsg) {
        mBestScoreMsg = bestScoreMsg;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    private void setupRectF() {
        float percentW = 0.5F;
        float percentH = 0.3F;
        mRectF.left = getCenterX() - 0.5F * percentW * getWidth();
        mRectF.top = getY() + 0.3F * getHeight() - 0.5F * percentH * getHeight();
        mRectF.right = mRectF.left + percentW * getWidth();
        mRectF.bottom = mRectF.top + percentH * getHeight();
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);

        setupRectF();

        //绘制矩形
        paint.setColor(Color.parseColor("#bbeeeeee"));
        canvas.drawRoundRect(mRectF, 0.1F * mRectF.width(), 0.1F * mRectF.height(), paint);


        float textSizeTitle = 0.16F * mRectF.height();
        float textSizeScore = 0.12F * mRectF.height();
        float textSizeBestScore = 0.1F * mRectF.height();
        float textSizeSubTitle = 0.15F * mRectF.height();

        paint.setColor(Color.BLACK);

        //绘制Game Over
        paint.setTextSize(textSizeTitle);
        float textX = getCenterX();
        float textY = mRectF.top + 0.28F * mRectF.height();
        if (!TextUtils.isEmpty(mTitle)) {
            canvas.drawText(mTitle, textX, textY, paint);
        }

        //绘制得分
        paint.setTextSize(textSizeScore);
        textY = mRectF.top + 0.6F * mRectF.height();
        if (!TextUtils.isEmpty(mScoreMsg)) {
            canvas.drawText(mScoreMsg, textX, textY, paint);
        }

        //绘制最高分
        paint.setTextSize(textSizeBestScore);
        textY = mRectF.top + 0.8F * mRectF.height();
        if (!TextUtils.isEmpty(mBestScoreMsg)) {
            canvas.drawText(mBestScoreMsg, textX, textY, paint);
        }

        //绘制click to restart
        paint.setTextSize(textSizeSubTitle);
        textY = getCenterY() + 0.24F * getHeight();
        if (!TextUtils.isEmpty(mSubTitle)) {
            canvas.drawText(mSubTitle, textX, textY, paint);
        }

    }
}
