package com.serenegiant.vo;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		MyLog.init();
		MyLog.log("MyApplication", "MyApplication onCreate");
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
	}

	public UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
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
				MyLog.log("yzk", info);
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
