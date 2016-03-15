package com.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.test.api.NT96650API;

public class TestActivity extends Activity implements OnClickListener {

	private Button btnOn;
	private Button btnOff;
	private Button btnReset;
	private Button btnLcmSiwtch;
	private Button btGPIO63H;
	private Button btGPIO63L;
	private Button btUsbCamera;
	private Button btUsbStorage;
	private static String TAG = "<zyx>";
	private TestInterface mTest;
	private NT96650API mNtApi;
	private static int lcmState;
	private final int usbStorageMode = 0x02;
	private final int usbCameraMode = 0x03;

	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);

		mTest = new TestInterface();
		mNtApi = new NT96650API();

		btnLcmSiwtch = (Button) findViewById(R.id.button1);
		btnLcmSiwtch.setOnClickListener(this);
		btnOn = (Button) findViewById(R.id.button2);
		btnOn.setOnClickListener(this);
		btnOff = (Button) findViewById(R.id.button3);
		btnOff.setOnClickListener(this);
		btnReset = (Button) findViewById(R.id.button4);
		btnReset.setOnClickListener(this);
		
		btGPIO63H = (Button) findViewById(R.id.button5);
		btGPIO63H.setOnClickListener(this);
		
		btGPIO63L = (Button) findViewById(R.id.button6);
		btGPIO63L.setOnClickListener(this);
		
		btUsbCamera = (Button) findViewById(R.id.button7);
		btUsbCamera.setOnClickListener(this);
		
		btUsbStorage = (Button) findViewById(R.id.button8);
		btUsbStorage.setOnClickListener(this);
		 
		// mTest.lcmSwitchTo655();
		lcmState = 1;
	}

	public void onClick(View v) {
		Log.i(TAG, "onClick");
		switch (v.getId()) {
		case R.id.button1:
			if (lcmState == 1) {
				mTest.lcmSwitchTo655();
				lcmState = 0;
			} else {
				mTest.lcmSwitchTo35();
				lcmState = 1;
			}
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;
		case R.id.button2:
			Log.i(TAG, "nt655PowerOn");
			mTest.nt655PowerOn();
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;
		case R.id.button3:
			Log.i(TAG, "nt655PowerOff");
			mTest.nt655PowerOff();
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;
		case R.id.button4:
			Log.i(TAG, "nt655Reset");
			mTest.nt655Reset();
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;
		case R.id.button5:
			Log.i(TAG, "setUsbMode host");
			mTest.setUsbMode(1);
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;
		case R.id.button6:
			Log.i(TAG, "setUsbMode device");
			mTest.setUsbMode(0);
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;	
		case R.id.button7:
			Log.i(TAG, "setUsbCamera");
			mNtApi.keyUsb(usbCameraMode);
			//mTest.setUsbMode(0);
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;	
		case R.id.button8:
			Log.i(TAG, "setUsbStorage");
			//mTest.setUsbMode(0);
			// Toast.makeText(this, (state ? "on" : "off"),0).show();
			break;	
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//mNtApi.stop();
		mTest.lcmSwitchTo35();
		lcmState = 1;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mNtApi.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mNtApi.stop();
	}
}