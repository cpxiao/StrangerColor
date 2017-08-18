package com.cpxiao.strangercolor.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.gamelib.mode.common.Sprite;
import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;
import com.cpxiao.strangercolor.mode.GameOverMsg;
import com.cpxiao.strangercolor.mode.GameState;
import com.cpxiao.strangercolor.mode.ScoreMsg;
import com.cpxiao.strangercolor.mode.block.Block;
import com.cpxiao.strangercolor.mode.block.BlockType;
import com.cpxiao.strangercolor.mode.extra.ColorExtra;
import com.cpxiao.strangercolor.mode.extra.Extra;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author cpxiao on 2017/08/17.
 */

public class GameView extends BaseSurfaceViewFPS {
    protected long mFrame = 0;

    protected ConcurrentLinkedQueue<Block> mBlockQueue = new ConcurrentLinkedQueue<>();
    private Block mLargeBlock;
    private Block[] mBottomBlockArray;
    private GameOverMsg mGameOverMsg;
    private ScoreMsg mScoreMsg;
    /**
     * 分数与最高分
     */
    protected int mScore = 0, mBestScore = 0;

    private int mColorCount = ColorExtra.COUNT_DEFAULT;

    private float mSpeed;

    private GameState mGameState = GameState.PREPARED;

    public GameView(Context context, int colorCount) {
        super(context);
        mColorCount = colorCount;
    }

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void restart() {
        mGameState = GameState.PREPARED;
        initWidget();
    }

    public void pause() {
        mGameState = GameState.PARSED;
    }

    public void resume() {
        mGameState = GameState.STARTED;
    }

    public void destroy() {
        if (mLargeBlock != null) {
            mLargeBlock.destroy();
            mLargeBlock = null;
        }

        if (mBottomBlockArray != null) {
            for (Block bottomBlock : mBottomBlockArray) {
                bottomBlock.destroy();
            }
            mBottomBlockArray = null;
        }

        if (!mBlockQueue.isEmpty()) {
            for (Block block : mBlockQueue) {
                block.destroy();
            }
            mBlockQueue.clear();
        }

    }

    @Override
    protected void timingLogic() {

    }

    @Override
    protected void initWidget() {
        if (DEBUG) {
            Log.d(TAG, "initWidget: ");
        }
        if (mColorCount <= 0) {
            if (DEBUG) {
                throw new IllegalArgumentException("param error! mColorCount <= 0");
            }
            return;
        }
        mBlockQueue.clear();
        mScore = 0;
        mBestScore = PreferencesUtils.getInt(getContext(), Extra.Key.getBestScoreKey(mBestScore), 0);


        /* 初始化速度 */
        mSpeed = 0.45f * mViewHeight / mFPS;

        /* 初始化底部小方块 */
        mBottomBlockArray = new Block[mColorCount];
        float bottomBlockW = 1.0F * mViewWidth / mColorCount;
        float bottomBlockH = 0.25F * mViewHeight;

        int[] array = ColorExtra.getColorArray(mColorCount);
        if (array == null || array.length != mColorCount) {
            if (DEBUG) {
                throw new IllegalArgumentException("param error!");
            }
            return;
        }
        int tmpIndex = (int) (Math.random() * 99);
        for (int i = 0; i < mColorCount; i++) {
            Block bottomBlock = new Block(BlockType.BottomBlock);
            bottomBlock.setWidth(bottomBlockW);
            bottomBlock.setHeight(bottomBlockH);
            bottomBlock.setX(i * bottomBlockW);
            bottomBlock.setY(mViewHeight - bottomBlockH);

            bottomBlock.setDrawRectFPercent(0.98F);
            int index = (tmpIndex + i) % array.length;
            String[] colorArray = getContext().getResources().getStringArray(array[index]);
            bottomBlock.setRectColor(Color.parseColor(colorArray[0]));
            bottomBlock.setString(colorArray[1]);
            //            mBlockQueue.add(bottomBlock);
            mBottomBlockArray[i] = bottomBlock;
        }


        /* 初始化底部大方块 */
        float largeBlockW = mViewWidth;
        float largeBlockH = 1 * bottomBlockH;

        mLargeBlock = new Block(BlockType.LargeBlock);
        mLargeBlock.setWidth(largeBlockW);
        mLargeBlock.setHeight(largeBlockH);
        mLargeBlock.setX(0);
        mLargeBlock.setY(mViewHeight - largeBlockH);
        mLargeBlock.setSpeedAndAngle(5 * mSpeed, 90);
        String[] colorArray = getContext().getResources().getStringArray(R.array.colorLargeBottom);
        mLargeBlock.setRectColor(Color.parseColor(colorArray[0]));
        mLargeBlock.setString(colorArray[1]);
        mLargeBlock.setStringColor(Color.parseColor(colorArray[2]));

        //        mBlockQueue.add(mLargeBlock);

        /* 初始化GameOverMsg */
        mGameOverMsg = new GameOverMsg();
        mGameOverMsg.setWidth(mViewWidth);
        mGameOverMsg.setHeight(mViewHeight);


        /* 初始化ScoreMsg*/
        mScoreMsg = new ScoreMsg();
        mScoreMsg.setWidth(mViewWidth);
        mScoreMsg.setHeight(0.2F * mViewWidth);
        mScoreMsg.setScoreMsg(String.valueOf(mScore));
    }

    @Override
    public void drawCache() {
        mFrame++;
        removeDestroyedBlock();
        if (DEBUG) {
            Log.d(TAG, "drawCache: " + mBlockQueue.size());
        }
        if (mGameState == GameState.PREPARED) {
            drawPrepared(mCanvasCache, mPaint);
        } else if (mGameState == GameState.STARTED) {
            //判断是否game over
            if (!mBlockQueue.isEmpty()) {
                Block block = mBlockQueue.peek();
                if (block.getCollideRectF().bottom > mBottomBlockArray[0].getCollideRectF().top) {
                    //Game Over
                    mGameState = GameState.GAME_OVER;
                }
            }

            drawStarted(mCanvasCache, mPaint);
        } else if (mGameState == GameState.PARSED) {
            drawParsed(mCanvasCache, mPaint);
        } else if (mGameState == GameState.GAME_OVER) {
            drawGameOver(mCanvasCache, mPaint);
        }

        mScoreMsg.onDraw(mCanvasCache, mPaint);

    }

    private void drawPrepared(Canvas canvas, Paint paint) {
        drawSpriteQueue(canvas, paint, false);
        drawBottomBlock(canvas, paint);
        mLargeBlock.onDraw(canvas, paint);
    }

    private void drawStarted(Canvas canvas, Paint paint) {
        createSprite();
        drawSpriteQueue(canvas, paint, true);
        drawBottomBlock(canvas, paint);
        mLargeBlock.draw(canvas, paint);
    }

    private void drawParsed(Canvas canvas, Paint paint) {
        drawSpriteQueue(canvas, paint, false);
        drawBottomBlock(canvas, paint);
    }

    private void drawGameOver(Canvas canvas, Paint paint) {
        drawSpriteQueue(canvas, paint, false);
        drawBottomBlock(canvas, paint);
        drawGameOverMsg(canvas, paint);
    }

    private void drawBottomBlock(Canvas canvas, Paint paint) {
        for (Sprite sprite : mBottomBlockArray) {
            sprite.draw(canvas, paint);
        }
    }

    private void drawSpriteQueue(Canvas canvas, Paint paint, boolean isMoving) {
        for (Sprite sprite : mBlockQueue) {
            if (isMoving) {
                sprite.draw(canvas, paint);
            } else {
                sprite.onDraw(canvas, paint);
            }
        }
    }

    private void drawGameOverMsg(Canvas canvas, Paint paint) {

        if (mGameOverMsg != null) {
            String scoreMsg = getContext().getString(R.string.score) + ": " + mScore;
            mGameOverMsg.setScoreMsg(scoreMsg);

            String bestScoreMsg = getContext().getString(R.string.best_score) + ": " + mBestScore;
            mGameOverMsg.setBestScoreMsg(bestScoreMsg);

            String title = getContext().getString(R.string.game_over);
            mGameOverMsg.setTitle(title);

            String subTitle = getContext().getString(R.string.tap_to_restart);
            mGameOverMsg.setSubTitle(subTitle);

            mGameOverMsg.draw(canvas, paint);
        }
    }

    private void removeDestroyedBlock() {
        //遍历sprites，绘制敌机、子弹、奖励、爆炸效果
        Iterator<Block> iterator = mBlockQueue.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            //检查Sprite是否超出了Canvas的范围，如果超出，则销毁Sprite
            RectF canvasRecF = new RectF(0, 0, mViewWidth, mViewHeight);
            RectF spriteRecF = block.getRectF();
            //在屏幕外且在屏幕底部才destroy
            if (!RectF.intersects(canvasRecF, spriteRecF) && spriteRecF.top > canvasRecF.bottom) {
                block.destroy();
            }

            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
            if (block.isDestroyed()) {
                //如果Sprite被销毁了，那么从Sprites中将其移除
                iterator.remove();
            }
        }
    }

    private float mCreateHeight = 9999;

    /**
     * 初始化一个颜色
     */

    private void createSprite() {
        if (mCreateHeight < 0.5F * mViewHeight) {
            mCreateHeight += mSpeed;
            return;
        }
        mCreateHeight = 0;

        //每生成一个，速度就加快一点点
        mSpeed = mSpeed + 0.2F;

        int[] array = ColorExtra.getColorArray(mColorCount);


        float colorStringW = mViewWidth;
        float colorStringH = 0.38F * colorStringW;


        Block block = new Block(BlockType.MovingBlock);
        block.setWidth(colorStringW);
        block.setHeight(colorStringH);
        block.centerTo(0.5f * mViewWidth, -0.5F * colorStringH);
        block.setSpeedAndAngle(mSpeed, 90);

        int i = (int) (Math.random() * 999 % array.length);
        String[] colorArray = getContext().getResources().getStringArray(array[i]);
        block.setRectColor(Color.parseColor(colorArray[0]));
        block.setString(colorArray[1]);
        int i2 = (int) (Math.random() * 99 % array.length);
        String[] colorArray2 = getContext().getResources().getStringArray(array[i2]);
        block.setStringColor(Color.parseColor(colorArray2[0]));
        mBlockQueue.add(block);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //        return super.onTouchEvent(event);
        if (DEBUG) {
            Log.d(TAG, "onTouchEvent: ");
        }

        int action = event.getAction();
        float eventX = event.getX();
        float eventY = event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            if (mGameState == GameState.PREPARED) {
                resume();
            } else if (mGameState == GameState.STARTED) {
                for (Block bottomBlock : mBottomBlockArray) {
                    if (bottomBlock.isClicked(eventX, eventY)) {
                        if (!mBlockQueue.isEmpty()) {
                            Block block = mBlockQueue.peek();
                            if (TextUtils.equals(block.getString(), bottomBlock.getString())) {
                                //点击正确，加分，销毁此block
                                mScore++;
                                mScoreMsg.setScoreMsg(String.valueOf(mScore));
                                if (mScore > mBestScore) {
                                    mBestScore = mScore;
                                    // 存储最高分
                                    PreferencesUtils.putInt(getContext(), Extra.Key.getBestScoreKey(mBestScore), mBestScore);
                                }
                                block.destroy();
                                break;
                            } else {
                                //点击错误，game over
                                mGameState = GameState.GAME_OVER;
                                return true;
                            }
                        }
                    }
                }

            } else if (mGameState == GameState.PARSED) {
                resume();
            } else if (mGameState == GameState.GAME_OVER) {
                if (eventY > 0.7F * mViewHeight) {
                    restart();
                }
            }
        }
        return true;
    }


}
