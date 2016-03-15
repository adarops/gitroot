package com.ilincar.drivingrecorder2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
/*		if(action.equals(Intent.ACTION_BOOT_COMPLETED)){
			Intent i = new Intent();
			i.setClass(context, MainService.class);
			i.putExtra("ui_visiable", false);	//开机启动，但不显示界面
			context.startService(i);
		}*/
	}

}
