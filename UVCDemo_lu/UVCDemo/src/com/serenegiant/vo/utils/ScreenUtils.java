package com.serenegiant.vo.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取屏幕的分辨率帮助类
 * @param context
 * @return width or height
 * @see ScreenUtils
 * */
public class ScreenUtils {
	private int width, height;
	private Context context;
	
	public ScreenUtils(Context context) {
		this.context = context;
		init();
	}

	private void init() {
		WindowManager wm = (WindowManager) context  
                .getSystemService(Context.WINDOW_SERVICE);  
        DisplayMetrics outMetrics = new DisplayMetrics();  
        wm.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
