package com.lizihanglove.game2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lizihanglove.game2048.util.Constant;
import com.lizihanglove.game2048.widget.AnimLayer;
import com.lizihanglove.game2048.widget.GameView;
import com.lizihanglove.game2048.R;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {

    public MainActivity() {
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
        root = (LinearLayout) findViewById(R.id.container);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);

        gameView = (GameView) findViewById(R.id.gameView);

        btnNewGame = (Button) findViewById(R.id.btn_restart);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             gameView.startGame();
            }
        });

        animLayer = (AnimLayer) findViewById(R.id.animLayer);
    }


    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();

        int maxScore = Math.max(score, getLastBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    public void saveBestScore(int s) {
        Editor e = getSharedPreferences(Constant.SP_NAME,MODE_PRIVATE).edit();
        e.putInt(Constant.SP_KEY_BEST_SCORE, s);
        e.commit();
    }

    public int getLastBestScore() {
        return getSharedPreferences(Constant.SP_NAME,MODE_PRIVATE).getInt(Constant.SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
    }

    private int score = 0;
    private TextView tvScore, tvBestScore;
    private LinearLayout root = null;
    private Button btnNewGame;
    private GameView gameView;
    private AnimLayer animLayer = null;

    private static MainActivity mainActivity = null;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }


    public void toSettingActivity(View view) {

        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    /*U-meng统计*/
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    /*U-meng统计*/
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
