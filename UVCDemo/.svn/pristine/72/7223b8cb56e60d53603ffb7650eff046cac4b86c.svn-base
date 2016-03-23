package com.serenegiant.vo;


import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import android.util.Log;

public class MyLog {
	public static String path;
	private static final String TAG = "UVCDemos";
	private static boolean DEBUG = true;

	public static void init() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy_MM_dd_hh_mm_ss");
		String date = sDateFormat.format(new java.util.Date());

		String dir = "";
		
		if (SDCardUtils.isSDCardEnable()) {
			dir = SDCardUtils.getSDCardPath() + TAG;
		} else {
			dir = SDCardUtils.getRootDirectoryPath();
		}
		 
		Log.e("log", dir);
		File f = new File(dir);
		if (!f.exists()) {
			Log.e("log..", dir);
			Log.i("mkdir",""+f.mkdirs());
		}
		
		path = dir + "/" + date + ".log";
	}

	public static void log(String Tag, String msg) {
		Log.d(TAG, Tag + " : " + msg);

		if (!DEBUG) {
			return;
		}

		FileWriter file;
		try {
			file = new FileWriter(path, true);
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			String date = sDateFormat.format(new java.util.Date());
			String s = date + " : " + Tag + " " + msg + "\n";
			file.write(s);
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
