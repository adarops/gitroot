package com.serenegiant;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import com.serenegiant.vo.utils.MyLog;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

/**
 * 捕获未捕获的异常，这个在manifest中指明application是这个MyApplication
 * @see MyApplication
 * */
public class MyApplication extends Application {

	List<Activity> list = new ArrayList<Activity>();  
	@Override
	public void onCreate() {
		super.onCreate();
		/**
		 * 捕获未捕获异常的异常
		 * */
//		MyLog.init();
//		MyLog.log("MyApplication", "MyApplication onCreate");
//		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}

	public UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Toast.makeText(getApplicationContext(), "(⊙o⊙)~ sorry ~请尝试重插外置摄像头！", Toast.LENGTH_SHORT).show();
			String info = null;
			ByteArrayOutputStream baos = null;
			PrintStream printStream = null;
			try {
				baos = new ByteArrayOutputStream();
				printStream = new PrintStream(baos);
				ex.printStackTrace(printStream);
				byte[] data = baos.toByteArray();
				info = new String(data);
				data = null;
				// 加后台接口
				MyLog.log("debug---", info);
				// android.os.Process.killProcess(android.os.Process.myPid());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (printStream != null) {
						printStream.close();
					}

					if (baos != null) {
						baos.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
	};

}
