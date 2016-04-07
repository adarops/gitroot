package com.serenegiant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	static final String action_boot = "android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(action_boot)) {
			//启动Service，后边的XXX.class就是要启动的服务  
//			Toast.makeText(context, "onreceive", Toast.LENGTH_SHORT).show();
//			Intent ootStartIntent = new Intent(context, HomeActivity.class);
//			ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(ootStartIntent);
			 //启动app，参数为需要自动启动的应用的包名
		    Intent outIntent = context.getPackageManager().getLaunchIntentForPackage("com.serenegiant.uvccamera");
		    context.startActivity(outIntent);      
		}
	}

}
