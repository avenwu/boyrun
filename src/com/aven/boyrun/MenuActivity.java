package com.aven.boyrun;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.waps.AdView;
import cn.waps.AppConnect;

import com.aven.boyrun.util.ConstantInfo;
import com.flurry.android.FlurryAgent;

public class MenuActivity extends Activity implements OnClickListener {

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, ConstantInfo.FLURRY_API_KEYSTRING);
        AppConnect.getInstance(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        AppConnect.getInstance(this).finalize();
    }

    private SharedPreferences mBaseSettings;//
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
        }

        public void onServiceDisconnected(ComponentName className) {
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置布局页面
        setContentView(R.layout.splash);
        initBtn();
        // 绑定srevice服务，ScoreUpgrateService
        Intent bindIntent = new Intent(this, ScoreUpgrateService.class);
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
        // 不指定名称，获取操作默认应用配置信息存储xml文件的SharedPreferences
        mBaseSettings = PreferenceManager.getDefaultSharedPreferences(this);

    }

    /**
     * 初始化按钮绑定与监听
     */
    public void initBtn() {
        LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout);
        new AdView(this,container).DisplayAd();
        final Button start_game = (Button) findViewById(R.id.start_game);
        final Button more_app = (Button) findViewById(R.id.more_app);
        final Button options = (Button) findViewById(R.id.options);
        final Button exit = (Button) findViewById(R.id.exit);

        start_game.setOnClickListener(this);
        more_app.setOnClickListener(this);
        options.setOnClickListener(this);
        exit.setOnClickListener(this);

        final AnimationDrawable ani = (AnimationDrawable) start_game.getBackground();
        final AnimationDrawable ani2 = (AnimationDrawable) more_app.getBackground();
        final AnimationDrawable ani3 = (AnimationDrawable) options.getBackground();
        final AnimationDrawable ani4 = (AnimationDrawable) exit.getBackground();
        // ani.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ani.start();
                ani2.start();
                ani3.start();
                ani4.start();
            }
        }, 200);
    }

    /**
     * 处理单击事件的操作，大致根据不同按启用不同的intent
     */
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {// 开始游戏
        case R.id.start_game:
            FlurryAgent.logEvent("start clicked");
            Log.i("Menu", "start game clicked");
            // 首先获取shareprefence的配置信息，判断是否开启“显示按键提示”功能
            if (mBaseSettings.getBoolean(ConstantInfo.PREFERENCE_KEY_SHOWTIPS, true)) {// TipsActivity中简单介绍游戏，下方有开始游戏按钮，启用AgileBuddyActivity
                i = new Intent(this, TipsActivity.class);
            } else {
                i = new Intent(this, AgileBuddyActivity.class);
            }
            break;
        // 设置选项
        case R.id.options:
            FlurryAgent.logEvent("Option clicked");
            Log.i("Menu", "option clicked");
            i = new Intent(this, Prefs.class);
            break;
        // 积分栏选项
        // case R.id.score_board:
        // i = new Intent(this, GlobalRankingActivity.class);
        // break;
        // 更多推荐选项
        case R.id.more_app:
            Toast.makeText(MenuActivity.this, "暂不提供", Toast.LENGTH_SHORT).show();
            // i = new Intent(Intent.ACTION_VIEW,
            // Uri.parse("market://search?q=pub:\"void1898\""));
            break;
        // 退出
        case R.id.exit:
            FlurryAgent.logEvent("exit clicked");
            Log.i("Menu", "exit clicked");
            finish();// 会重写
            return;
        }
        if (i != null) {// 跳转到指定activity
            startActivity(i);
        }
    }

    @Override
    public void finish() {
        this.unbindService(mConnection);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Builder builder = new Builder(MenuActivity.this);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        Dialog dialog = builder.setTitle("退出").setMessage("确定要退出游戏吗？").create();
        dialog.show();
    }
}
