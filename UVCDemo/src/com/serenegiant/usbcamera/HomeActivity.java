package com.serenegiant.usbcamera;

import com.serenegiant.uvccamera.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_home);
	}
	
	public void toSelect(View view) {
		openActivity(ImageDealActivity.class);
	}
	
	public void toCapture(View view) {
		openActivity(CaptureActivity.class);
	}
	
	private void openActivity(Class<?> cls) {
		Intent mIntent = new Intent(HomeActivity.this, cls);
		startActivity(mIntent);
	}
}
