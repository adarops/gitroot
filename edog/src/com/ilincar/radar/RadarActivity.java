package com.ilincar.radar;


import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;


public class RadarActivity extends Activity{
	
	public static Activity instance;
	public static Context content;
    private Switch mRadarSwitch = null;
	private long exitTime = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.main);
        mRadarSwitch = (Switch) findViewById(R.id.radar_switch);
        
        SharedPreferences sp = getSharedPreferences("RadarData",Activity.MODE_PRIVATE);
        boolean mRadarEnable = sp.getBoolean( "radaenable",false );
        if(mRadarEnable){
        	mRadarSwitch.setChecked(true);
        	RadarOpen();
        }
        else{
        	mRadarSwitch.setChecked(false);
        }

        mRadarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(CompoundButton buttonView,  
                    boolean isChecked) {  
                if (isChecked) {
                	RadarOpen();
                } else {
                	RadarClose();
                }  
            }  
        });  
        	
//		finish();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		System.exit(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void showToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void RadarOpen(){
		showToast("雷达已打开");
		SharedPreferences d=getSharedPreferences("RadarData",Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = d.edit();
		editor.putBoolean("radaenable",true);
		editor.commit();
		
		Intent intent = new Intent();
		intent.setClass(this, RadarService.class);
		startService(intent);
		
//		String br = this.getString(R.string.baudrate);
//		Intent intent1 = new Intent();
//		intent1.putExtra("uartfunction", "radar");
//		intent1.putExtra("uartdevice", 3);
//		intent1.putExtra("baudrate", Integer.parseInt(br));
//		intent1.setClass(this, SerialPortService.class);
//		startService(intent1);
	}
	
	public void RadarClose(){
		showToast("雷达已关闭");
		SharedPreferences d=getSharedPreferences("RadarData",Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = d.edit();
		editor.putBoolean("radaenable",false);
		editor.commit();

		Intent intent = new Intent();
		intent.setClass(this, RadarService.class);
		stopService(intent);
		
//		Intent intent1 = new Intent();
//		intent1.setClass(this, SerialPortService.class);
//		stopService(intent1);
		
//		stopService(new Intent("SerialPortService.class"));   //这个不行
	}



}
