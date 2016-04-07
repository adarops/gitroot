package com.android.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StaticBroadcastReceiver extends BroadcastReceiver {
	private final String TAG = "zyx";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.i(TAG, "receive ACTION_BOOT_COMPLETED broadcast");
			Intent new_intent = new Intent(context, TestActivity.class);
			// popup the activity
			new_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(new_intent);
		} else if (action.equals("com.android.server.am.AppErrorDialog")) {
			Log.i(TAG, "receive AppErrorDialog broadcast");
		}
	}
}
