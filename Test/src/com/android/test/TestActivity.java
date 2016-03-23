package com.android.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity implements SensorEventListener {

	private static String TAG = "zyx";
	private TestInterface mTest;
	private Button button1;
	private Context mContext;
	private TextView gText1, gText2, gText3, gText4, gText5, gText6;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;;

	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);

		mContext = getApplicationContext();
		mTest = new TestInterface();

		IntentFilter filter = new IntentFilter();
		filter.addAction("action_change_txzvoice_orc");
		filter.addAction("com.android.internal.policy.impl.camerakey");
		mContext.registerReceiver(mIntentReceiver, filter, null, null);

		gText1 = (TextView) findViewById(R.id.auto_gsensor_step1);
		gText2 = (TextView) findViewById(R.id.auto_gsensor_step2);
		gText3 = (TextView) findViewById(R.id.auto_gsensor_step3);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals("action_change_txzvoice_orc")) {
				Log.i(TAG, "receive voice broadcast");
			} else if (action
					.equals("com.android.internal.policy.impl.camerakey")) {
				Log.i(TAG, "receive takePicture broadcast");
			}
		}
	};
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//TYPE_LINEAR_ACCELERATION 	TYPE_ACCELEROMETER
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) { 
			Log.d(TAG, "onSensorChanged,gText1,gText2,gText3" + gText1 + ","
					+ gText2 + "," + gText3);
			gText1.setText("  X: " + event.values[0] + " m/s^2");
			gText2.setText("  Y: " + event.values[1] + " m/s^2");
			gText3.setText("  Z: " + event.values[2] + " m/s^2");
		}

	}

	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA:
			// Log.i(TAG, "key_camera press");
			// Intent broadcastIntent=new Intent();
			// broadcastIntent.setAction("takePicture");
			// mContext.sendBroadcast(broadcastIntent);
			break;
		case 230:
			Log.i(TAG, "key_voice press");
			break;
		}
		return super.onKeyUp(keyCode, event);
	};

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

}
