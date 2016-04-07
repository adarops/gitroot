package com.serenegiant.ui;

import com.serenegiant.uvccamera.R;
import com.serenegiant.vo.Debug;
import com.serenegiant.vo.utils.CameraSizeHelper;
import com.serenegiant.vo.utils.MyLog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 首页页面
 * @see HomeActivity
 * */
public class HomeActivity extends Activity {
	private Button mCaptureButton;
	
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
	private boolean isTesting = true;  
	
	ProgressDialog m_pDialog;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码屏蔽home键
		setContentView(R.layout.layout_home);
		
		/** 
		 * 获取Camera支持的分辨率 
		 * */
		if (Debug.isOpen) {
			MyLog.log("Camera Support Size: ", CameraSizeHelper.getCameraSupportSize());
		}
		
		mCaptureButton = (Button) findViewById(R.id.to_scan_btn);

		mCaptureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toCapture();
			}
		});
	}
	

	private void toCapture() {
		openActivity(CaptureActivity.class);
	}

	private void openActivity(Class<?> cls) {
		Intent mIntent = new Intent(HomeActivity.this, cls);
		startActivity(mIntent);
	}
}
