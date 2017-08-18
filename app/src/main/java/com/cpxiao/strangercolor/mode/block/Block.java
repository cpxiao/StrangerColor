package com.cpxiao.strangercolor.mode.block;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.cpxiao.gamelib.mode.common.LifeSprite;


/**
 * 方块类
 *
 * @author cpxiao on 2017/8/17.
 */

public class Block extends LifeSprite {

    private int mRectColor;
    private int mStringColor;
    private String mString;
    private BlockType mBlockType;

    public Block(BlockType blockType) {
        mBlockType = blockType;
    }

    public void setRectColor(int rectColor) {
        mRectColor = rectColor;
    }

    public void setStringColor(int stringColor) {
        mStringColor = stringColor;
    }

    public void setString(String string) {
        mString = string;
    }

    public String getString() {
        return mString;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);
        if (getLife() <= 0) {
            return;
        }
        if (mBlockType == BlockType.BottomBlock) {
            drawRect(canvas, paint);
        } else if (mBlockType == BlockType.LargeBlock) {
            paint.setFakeBoldText(false);
            drawRect(canvas, paint);
            drawString(canvas, paint);
        } else if (mBlockType == BlockType.MovingBlock) {
            paint.setFakeBoldText(true);
            drawString(canvas, paint);
        }
    }

    private void drawRect(Canvas canvas, Paint paint) {
        float rXY = 0.05F * getHeight();
        paint.setColor(mRectColor);
        canvas.drawRoundRect(getDrawRectF(), rXY, rXY, paint);
    }

    private void drawString(Canvas canvas, Paint paint) {
        float percent = 0.3F;
        setCollideRectFPercent(percent);
        paint.setTextSize(percent * getHeight());
        paint.setColor(mStringColor);
        canvas.drawText(getString(), getCenterX(), getCenterY() + 0.14f * getHeight(), paint);
    }

}
