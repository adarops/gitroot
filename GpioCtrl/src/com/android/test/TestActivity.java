package com.android.test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.io.GpioCtrl;

public class TestActivity extends Activity implements OnClickListener {

	private Button btnOn;
	private Button btnOff;
	private Button btnReset;
	private Button btnLcmSiwtch;
	private Button btBTOn;
	private Button btBTOff;
	private Button testButton;
	private static boolean testState;
	private static String TAG = "zyx";
	private GpioCtrl mTest;
	private static int lcmState;

	protected void onCreate(Bundle state) {
		super.onCreate(state);

		setContentView(R.layout.main);

		mTest = new GpioCtrl();
	
		btnLcmSiwtch = (Button) findViewById(R.id.button1);
		btnLcmSiwtch.setOnClickListener(this);
		btnOn = (Button) findViewById(R.id.button2);
		btnOn.setOnClickListener(this);
		btnOff = (Button) findViewById(R.id.button3);
		btnOff.setOnClickListener(this);
		btnReset = (Button) findViewById(R.id.button4);
		btnReset.setOnClickListener(this);
		btBTOn = (Button) findViewById(R.id.button5);
		btBTOn.setOnClickListener(this);
		btBTOff = (Button) findViewById(R.id.button6);
		btBTOff.setOnClickListener(this);
		testButton = (Button) findViewById(R.id.button7);
		testButton.setOnClickListener(this);
		/*
		 * btBlOff = (Button) findViewById(R.id.button5);
		 * btBlOff.setOnClickListener(this);
		 */

		// mTest.lcmSwitchTo655();
		lcmState = 1;
		testState = false;

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
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mTest.lcmSwitchTo35();
		lcmState = 1;
	}

}