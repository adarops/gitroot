package com.android.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {

	private static String TAG = "zyx";
	private TestInterface mTest;
	private Button button1;
	private Context mContext;
	private TextView gText1, gText2, gText3, gText4, gText5, gText6;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mLinear;

	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);
		Log.d(TAG, "onCreate");
		mContext = getApplicationContext();
		mTest = new TestInterface();

		IntentFilter filter = new IntentFilter();
		filter.addAction("action_change_txzvoice_orc");
		filter.addAction("com.android.internal.policy.impl.camerakey");
		filter.addAction("com.android.server.am.AppErrorDialog");
		mContext.registerReceiver(mIntentReceiver, filter, null, null);

		/*gText1 = (TextView) findViewById(R.id.auto_gsensor_step1);
		gText2 = (TextView) findViewById(R.id.auto_gsensor_step2);
		gText3 = (TextView) findViewById(R.id.auto_gsensor_step3);
		gText4 = (TextView) findViewById(R.id.auto_gsensor_step4);
		gText5 = (TextView) findViewById(R.id.auto_gsensor_step5);
		gText6 = (TextView) findViewById(R.id.auto_gsensor_step6);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mLinear = mSensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);*/
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Log.d(TAG, "new thread");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.d(TAG, "new thread 1");
				mTest.lcmSwitchTo35();
				Log.d(TAG, "new thread 2");
			}
		}).start();
		
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
			} else if (action
					.equals("com.android.server.am.AppErrorDialog")) {
				Log.i(TAG, "receive AppErrorDialog broadcast");
			}
		}
	};

	/*@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}*/

	/*@Override
	public void onSensorChanged(SensorEvent event) {
		// TYPE_LINEAR_ACCELERATION TYPE_ACCELEROMETER
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			gText1.setText("LINEAR X: " + event.values[0] + " m/s^2");
			gText2.setText("LINEAR Y: " + event.values[1] + " m/s^2");
			gText3.setText("LINEAR Z: " + event.values[2] + " m/s^2");
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			gText4.setText("ACCEL X: " + event.values[0] + " m/s^2");
			gText5.setText("ACCEL Y: " + event.values[1] + " m/s^2");
			gText6.setText("ACCEL Z: " + event.values[2] + " m/s^2");
		}
	}*/

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
		/*mSensorManager.registerListener(this, mLinear,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);*/
	}

	protected void onPause() {
		super.onPause();
//		mSensorManager.unregisterListener(this);
	}

}
