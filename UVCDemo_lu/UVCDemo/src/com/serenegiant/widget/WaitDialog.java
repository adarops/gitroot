package com.serenegiant.widget;

import com.serenegiant.uvccamera.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 扫描等待对话框
 * @see WaitDialog
 * */
public class WaitDialog extends AlertDialog{

	public WaitDialog(Context context) {
		super(context);
	}

	public void showDialog() {
		init();
	}
	
	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_wait_dialog, null);
		this.setView(view);
		this.setCancelable(false);
		this.show();
	}
}
