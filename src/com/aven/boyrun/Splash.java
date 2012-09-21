package com.aven.boyrun;

import java.util.Timer;
import java.util.TimerTask;

import com.aven.boyrun.util.ConstantInfo;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @author void1898@gmail.com ����Ϸԭ��ΪAgileBuddy��������е��޸Ĳ�����Ϊѧϰ�о�֮��
 * @author iamavenwu@gmail.com
 * 
 */
public class Splash extends Activity implements OnClickListener
{

	private SharedPreferences mBaseSettings;//
	private ServiceConnection mConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service)
		{
		}

		public void onServiceDisconnected(ComponentName className)
		{
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		// ���ô��ڷ���ޱ��⣬��Ļ���ֳ�����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ���ò���ҳ��
		setContentView(R.layout.splash);

		initBtn();
		// ��srevice����ScoreUpgrateService
		Intent bindIntent = new Intent(this, ScoreUpgrateService.class);
		bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
		// ��ָ�����ƣ���ȡ����Ĭ��Ӧ��������Ϣ�洢xml�ļ���SharedPreferences
		mBaseSettings = PreferenceManager.getDefaultSharedPreferences(this);
	}

	/**
	 * ��ʼ����ť�������
	 */
	public void initBtn()
	{

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
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				ani.start();
				ani2.start();
				ani3.start();
				ani4.start();
			}
		}, 200);
	}

	/**
	 * �������¼��Ĳ��������¸��ݲ�ͬ�����ò�ͬ��intent
	 */
	public void onClick(View v)
	{
		Intent i = null;
		switch (v.getId())
		{// ��ʼ��Ϸ
		case R.id.start_game:
			// ���Ȼ�ȡshareprefence��������Ϣ���ж��Ƿ�������ʾ������ʾ������
			if (mBaseSettings.getBoolean(ConstantInfo.PREFERENCE_KEY_SHOWTIPS, true))
			{// TipsActivity�м򵥽�����Ϸ���·��п�ʼ��Ϸ��ť������AgileBuddyActivity
				i = new Intent(this, TipsActivity.class);
			} else
			{
				i = new Intent(this, AgileBuddyActivity.class);
			}
			break;
		// ����ѡ��
		case R.id.options:
			i = new Intent(this, Prefs.class);
			break;
		// ������ѡ��
		// case R.id.score_board:
		// i = new Intent(this, GlobalRankingActivity.class);
		// break;
		// �����Ƽ�ѡ��
		case R.id.more_app:
			Toast.makeText(Splash.this, "�ݲ��ṩ", Toast.LENGTH_SHORT).show();
			// i = new Intent(Intent.ACTION_VIEW,
			// Uri.parse("market://search?q=pub:\"void1898\""));
			break;
		// �˳�
		case R.id.exit:
			finish();// ����д
			return;
		}
		if (i != null)
		{// ��ת��ָ��activity
			startActivity(i);
		}
	}

	@Override
	public void finish()
	{
		this.unbindService(mConnection);
		super.finish();
	}

	@Override
	public void onBackPressed()
	{
		Builder builder = new Builder(Splash.this);

		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				return;
			}
		});
		Dialog dialog = builder.setTitle("�˳�").setMessage("ȷ��Ҫ�˳���Ϸ��").create();
		dialog.show();
	}
}
