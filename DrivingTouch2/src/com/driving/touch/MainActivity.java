package com.driving.touch;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.test.TestInterface;
import com.driving.touch.api.NT96650API;

public class MainActivity extends Activity {

	private NT96650API mNtApi;
	private static int lcmState;
	private static boolean nt655Power;
	private static boolean tvoutEnable;
	private TestInterface mTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TypedArray buttons = getResources().obtainTypedArray(
				R.array.camera_buttons);
		for (int i = 0; i < buttons.length(); i++) {
			setOnClickListener(null, buttons.getResourceId(i, 0));
		}

		mNtApi = new NT96650API();
		mTest = new TestInterface();
		lcmState = 1;
		nt655Power = true;
		tvoutEnable = false;
	}

	private void setOnClickListener(View root, int id) {
		final View target = root != null ? root.findViewById(id)
				: findViewById(id);
		target.setOnClickListener(onClickListener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mNtApi.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mNtApi.stop();
		mTest.lcmSwitchTo35();
		lcmState = 1;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mNtApi.stop();
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int state = -1;
			switch (v.getId()) {
			case R.id.btn_lock:
				Log.d("zoufeng", "R.id.btn_lock");
				mNtApi.keyCameraLock();
				break;
			case R.id.btn_mic:
				Log.d("zoufeng", "R.id.btn_mic");
				state = mNtApi.keyMic();
				break;
			case R.id.btn_recorder_enable:
				Log.d("zoufeng", "R.id.btn_recorder_enable");
				state = mNtApi.keyCamera();
				break;
			case R.id.btn_switch_picinpic:
				Log.d("zoufeng", "R.id.btn_switch_picinpic");
				state = mNtApi.keyPIP();
				break;
			case R.id.btn_back_ui:
				Log.d("zoufeng", "R.id.btn_back_ui");
				state = mNtApi.keyBackAndroidUI();
				break;
			case R.id.btn_settings:
				Log.d("zoufeng", "R.id.btn_settings");
				state = mNtApi.keySetTwoLevelMenu();
				break;
			case R.id.btn_playback_menu:
				Log.d("zoufeng", "R.id.btn_playback_menu");
				state = mNtApi.keyReviewTwoLevelMenu();
				break;
			case R.id.btn_take_photo:
				Log.d("zoufeng", "R.id.btn_take_photo");
				state = mNtApi.keyPhoto();
				break;
			case R.id.btn_get_photo:
				Log.d("zoufeng", "R.id.btn_get_photo");
				int index = 0;
				mNtApi.getPhotoFile(index);
				break;
			case R.id.btn_up:
				Log.d("zoufeng", "R.id.btn_up");
				state = mNtApi.keyUp();
				break;
			case R.id.btn_down:
				Log.d("zoufeng", "R.id.btn_down");
				state = mNtApi.keyDown();
				break;
			case R.id.btn_ok:
				Log.d("zoufeng", "R.id.btn_ok");
				state = mNtApi.keyOk();
				break;
			case R.id.btn_back_previous:
				Log.d("zoufeng", "R.id.btn_back_previous");
				state = mNtApi.keyBack();
				break;
			case R.id.btn_sync_date:
				Log.d("zoufeng", "R.id.btn_sync_date");
				state = mNtApi.syncDate(0x07DF, 0x0C, 0x02, 0x13, 0x15, 0x2f);
				break;
			case R.id.btn_syn_watermark:
				Log.d("zoufeng", "R.id.btn_syn_watermark");
				state = mNtApi.syncCarInfo("粤B3M6ZF");
				break;
			case R.id.btn_shaking_hands:
				Log.d("zoufeng", "R.id.btn_shaking_hands");
				state = mNtApi.link();
				break;
			case R.id.btn_backlight_adjustment:
				Log.d("zoufeng", "R.id.btn_backlight_adjustment");
				state = mNtApi.keyBright(255);
				break;
			case R.id.lcm_switch:
				Log.d("zoufeng", "R.id.lcm_switch");
				if (lcmState == 1) {
					mTest.lcmSwitchTo655();
					lcmState = 0;
				} else {
					mTest.lcmSwitchTo35();
					lcmState = 1;
					state = 0;
				}
				break;
			case R.id.btn_power:
				Log.d("zoufeng", "R.id.btn_power");
				if (nt655Power) {
					state = mNtApi.keyPower(false);
					nt655Power = false;
				} else {
					state = mNtApi.keyPower(true);
					nt655Power = true;
				}
				break;
			case R.id.btn_tvout:
				Log.d("zoufeng", "R.id.btn_tvout");
				if (tvoutEnable) {
					state = mNtApi.keyTvout(false);
					tvoutEnable = false;
				} else {
					state = mNtApi.keyTvout(true);
					tvoutEnable = true;
				}
				break;
			}
			// showState(state);
		}
	};

	private void showState(int state) {
		Toast.makeText(this, "返回状态为:" + state, Toast.LENGTH_SHORT).show();
	}
}
