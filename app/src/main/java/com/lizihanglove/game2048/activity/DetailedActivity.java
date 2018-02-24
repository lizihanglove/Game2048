package com.lizihanglove.game2048.activity;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lizihanglove.game2048.R;

import static android.R.id.input;

/**
 * Created by lizihanglove on 2017/3/5.
 */

public class DetailedActivity extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private TextView website;
    private TextView wechat;
    private TextView weibo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
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
        website = (TextView) findViewById(R.id.tv_website);
        wechat = (TextView) findViewById(R.id.tv_wechat);
        weibo = (TextView) findViewById(R.id.tv_weibo);

    }

    public void clipWebsite(View view) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        String text = website.getText().toString().trim();
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(),"复制成功",Toast.LENGTH_SHORT).show();
    }
    public void clipWechat(View view) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        String text = wechat.getText().toString().trim();
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(),"复制成功",Toast.LENGTH_SHORT).show();
    }
    public void clipWeibo(View view) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        String text = weibo.getText().toString().trim();
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(),"复制成功",Toast.LENGTH_SHORT).show();
    }

}
