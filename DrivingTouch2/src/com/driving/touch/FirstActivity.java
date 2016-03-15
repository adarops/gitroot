package com.driving.touch;

import com.android.test.TestInterface;
import com.driving.touch.api.NT96650API;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class FirstActivity extends BaseActivity {
	
	public static NT96650API mNtApi;
	public static int lcmState;
	public static boolean nt655Power;
	public static boolean tvoutEnable;
	public static TestInterface mTest;
	private static String homeKey="com.android.internal.policy.impl.homekey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		mNtApi = new NT96650API();
		mTest = new TestInterface();
		lcmState = 1;
		nt655Power = true;
		tvoutEnable = false;
		
		final TypedArray buttons = getResources().obtainTypedArray(
				R.array.first_buttons);
		for (int i = 0; i < buttons.length(); i++) {
			setOnClickListener(null, buttons.getResourceId(i, 0));
		}
//		mTest.lcmSwitchTo655();
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(homeKey);
		registerReceiver(receiver, filter);
	}
	
	private void setOnClickListener(View root, int id) {
		final View target = root != null ? root.findViewById(id)
				: findViewById(id);
		target.setOnClickListener(onClickListener);
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
				finish();
				state = mNtApi.keyBackAndroidUI();
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						Thread.currentThread();
//						try {
//							Thread.sleep(500);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						mTest.lcmSwitchTo35();
//					}
//				}).start();
				break;
			case R.id.btn_settings:
				Log.d("zoufeng", "R.id.btn_settings");
				state = mNtApi.keySetTwoLevelMenu();
				Intent intent=new Intent(FirstActivity.this,SecondActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_playback_menu:
				Log.d("zoufeng", "R.id.btn_playback_menu");
				state = mNtApi.keyReviewTwoLevelMenu();
				Intent intent1=new Intent(FirstActivity.this,ThirdActivity.class);
				startActivity(intent1);
				break;
			case R.id.btn_take_photo:
				Log.d("zoufeng", "R.id.btn_take_photo");
				state = mNtApi.keyPhoto();
				break;
			}
		}
	};
	
	BroadcastReceiver receiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			Log.d("wjz", "action="+action);
			if(homeKey.equals(action)){
				lcmState = 1;
				mTest.lcmSwitchTo35();
			}
			else if(Intent.ACTION_SCREEN_OFF.equals(action)){
				mTest.lcmSwitchTo35();
			}
		}
	};
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("wjz", "onStart(1)_lcmState="+lcmState);
		if (lcmState == 1) {
			mTest.lcmSwitchTo655();
			lcmState = 0;
		}
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
//		mNtApi.stop();
//		mTest.lcmSwitchTo35();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mNtApi.stop();
		mTest.lcmSwitchTo35();
	}
}
