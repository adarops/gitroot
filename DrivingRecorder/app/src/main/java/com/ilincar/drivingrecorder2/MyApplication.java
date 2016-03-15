package com.ilincar.drivingrecorder2;

import java.util.Iterator;
import java.util.List;

import com.closeli.camera.Closeli;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

public class MyApplication extends Application {
	// 云存储账号登陆状态
	public static boolean LOGIN_STATE = false;
	private static MyApplication application;
	String TAG = "MyApplicationliujh";

	@Override
	public void onCreate() {
		super.onCreate();
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		Log.d(TAG, "processAppName:" + processAppName + ",getPackageName:"
				+ getPackageName());
		if (processAppName == null
				|| !processAppName.equalsIgnoreCase(getPackageName()))
			return;
		application = this;
		Closeli.getInstance().init(application);
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}


	public static MyApplication getApplication() {
		return application;
	}

}
