package com.lizihanglove.game2048.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lizihanglove.game2048.Bean.VersionBean;
import com.lizihanglove.game2048.R;
import com.lizihanglove.game2048.util.Constant;
import com.lizihanglove.game2048.widget.DiyDialog;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Created by lizihanglove on 2017/3/4.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isVibrate;
    private boolean isPlayMusic;
    private Button vibrate;
    private Button playMusic;
    private SharedPreferences.Editor editor;
    private Button updateButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
        initView();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void initView() {
        isVibrate = getSharedPreferences(Constant.SP_NAME, MODE_APPEND).getBoolean(Constant.IS_VIBRATE, false);

        isPlayMusic = getSharedPreferences(Constant.SP_NAME, MODE_APPEND).getBoolean(Constant.IS_PLAY_MUSIC, false);


        vibrate = (Button) findViewById(R.id.btn_vibrate);
        if (isVibrate) {
            vibrate.setText("关闭震动");
        } else {
            vibrate.setText("开启震动");
        }
        playMusic = (Button) findViewById(R.id.btn_play_music);
        if (isPlayMusic) {
            playMusic.setText("关闭音乐");
        } else {
            playMusic.setText("开启音乐");
        }

        vibrate.setOnClickListener(this);
        playMusic.setOnClickListener(this);
        updateButton = (Button) findViewById(R.id.btn_update);
        updateButton.setOnClickListener(this);

        findViewById(R.id.btn_about).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_vibrate:
                if (isVibrate) {
                    editor = getSharedPreferences(Constant.SP_NAME, MODE_APPEND).edit();
                    editor.putBoolean(Constant.IS_VIBRATE, false).commit();
                    vibrate.setText("开启震动");
                    isVibrate = !isVibrate;
                } else {
                    editor = getSharedPreferences(Constant.SP_NAME, MODE_APPEND).edit();
                    editor.putBoolean(Constant.IS_VIBRATE, true).commit();
                    vibrate.setText("关闭震动");
                    isVibrate = !isVibrate;
                }

                break;
            case R.id.btn_play_music:
                if (isPlayMusic) {
                    editor = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(Constant.IS_PLAY_MUSIC, false).commit();
                    playMusic.setText("开启音乐");
                    isPlayMusic = !isPlayMusic;
                } else {
                    editor = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(Constant.IS_PLAY_MUSIC, true).commit();
                    playMusic.setText("关闭音乐");
                    isPlayMusic = !isPlayMusic;
                }
                break;
            case R.id.btn_update:
                updateButton.setEnabled(false);
                updateApp();
                updateButton.setEnabled(true);
                break;
            case R.id.btn_about:
                toDetailedActivity();
                break;

        }

    }

    private void toDetailedActivity() {
        startActivity(new Intent(SettingActivity.this,DetailedActivity.class));
    }

    private void updateApp() {
        final int recentVersionCode = getRecentVersionCode(SettingActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Constant.UPDATE_VERSION)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String string = response.body().string();
                    VersionBean versionBean = new Gson().fromJson(string, VersionBean.class);
                    int latestVersionCode = versionBean.getVersionCode();
                    if (latestVersionCode > recentVersionCode) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DiyDialog.Builder builder = new DiyDialog.Builder(SettingActivity.this);
                                builder.setTitle("版本更新")
                                        .setMessage("")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Toast.makeText(SettingActivity.this,"正在维护",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this,"版本无更新",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getRecentVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    getPackageName(context), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getRecentVersionName(Context context) {

        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    getPackageName(context), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verName;
    }

    public static String getPackageName(Context context) {

        return context.getPackageName();

    }


}
