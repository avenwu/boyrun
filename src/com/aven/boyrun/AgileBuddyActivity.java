package com.aven.boyrun;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import cn.waps.AdView;

public class AgileBuddyActivity extends Activity {
    private AgileBuddyView mAgileBuddyView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        LinearLayout container = (LinearLayout) findViewById(R.id.AdLinearLayout);
        new AdView(this, container).DisplayAd();
        mAgileBuddyView = (AgileBuddyView) findViewById(R.id.agile_buddy);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent e) {
                mAgileBuddyView.handleMoving(e.values[SensorManager.DATA_X]);
            }

            public void onAccuracyChanged(Sensor s, int accuracy) {
            }
        };
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void finish() {
        mSensorManager.unregisterListener(mSensorEventListener, mSensor);
        super.finish();
    }
}