package com.cpxiao.strangercolor.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/08/18.
 */

public class ScoreMsg extends Sprite {
    private String mScoreMsg;

    public void setScoreMsg(String scoreMsg) {
        mScoreMsg = scoreMsg;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);

        float textSizeScore = 0.5F * getHeight();

        //绘制Game Over
        float textX = getCenterX();
        float textY = getCenterY() + 0.24F * getHeight();
        //绘制得分
        paint.setTextSize(textSizeScore);
        if (!TextUtils.isEmpty(mScoreMsg)) {
            paint.setFakeBoldText(false);
            paint.setColor(Color.BLACK);
            canvas.drawText(mScoreMsg, textX, textY, paint);
        }


    }
}
