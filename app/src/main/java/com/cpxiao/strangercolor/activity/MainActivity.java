package com.cpxiao.strangercolor.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cpxiao.R;
import com.cpxiao.strangercolor.mode.SceneState;
import com.cpxiao.strangercolor.mode.extra.ColorExtra;
import com.cpxiao.strangercolor.views.GameView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SceneState mSceneState = SceneState.Home;

    private LinearLayout mLayoutHome;
    private LinearLayout mLayoutGame;
    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutHome = (LinearLayout) findViewById(R.id.layout_home);
        mLayoutGame = (LinearLayout) findViewById(R.id.layout_game);

        Button easyBtn = (Button) findViewById(R.id.easy);
        Button normalBtn = (Button) findViewById(R.id.normal);
        Button hardBtn = (Button) findViewById(R.id.hard);
        Button quitBtn = (Button) findViewById(R.id.quit);

        easyBtn.setOnClickListener(this);
        normalBtn.setOnClickListener(this);
        hardBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mGameView != null) {
//            mGameView.resume();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGameView != null) {
            mGameView.pause();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.easy) {
            initGameView(ColorExtra.COUNT_3);
        } else if (id == R.id.normal) {
            initGameView(ColorExtra.COUNT_4);
        } else if (id == R.id.hard) {
            initGameView(ColorExtra.COUNT_5);
        } else if (id == R.id.quit) {
            showQuitConfirmDialog();
        }

    }

    private void initGameView(int colorCount) {
        mSceneState = SceneState.Game;

        mLayoutHome.setVisibility(View.GONE);
        mLayoutGame.setVisibility(View.VISIBLE);

        if (mGameView != null) {
            mGameView.destroy();
            mGameView = null;
        }
        mGameView = new GameView(this, colorCount);
        mLayoutGame.removeAllViews();
        mLayoutGame.addView(mGameView);
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
        if (mSceneState == SceneState.Home) {
            showQuitConfirmDialog();
        } else if (mSceneState == SceneState.Game) {
            mSceneState = SceneState.Home;
            mLayoutHome.setVisibility(View.VISIBLE);
            mLayoutGame.setVisibility(View.GONE);
        }
    }

    private void showQuitConfirmDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                //                .setTitle(R.string.quit_msg)
                .setMessage(R.string.quit_msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        //            dialog.setCancelable(true);
        //            dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
